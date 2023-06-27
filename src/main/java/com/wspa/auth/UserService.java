package com.wspa.auth;

import com.wspa.auth.exceptions.AuthorizationException;
import com.wspa.auth.exceptions.UserNotFoundException;
import com.wspa.auth.exceptions.UserNotLoggedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
@PropertySource("classpath:variables.properties")
class UserService {
    private final int SESSION_TIME;
    private int BCRYPT_STRENGTH = 10;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(BCRYPT_STRENGTH);

    private final UserDatabase userDatabase;
    private final AuthRedisTemplate authRedisTemplate;

    UserService(@Value("${SESSION_TIME}") int SESSION_TIME, @Value("${BCRYPT_STRENGTH}") int BCRYPT_STRENGTH, UserDatabase userDatabase, AuthRedisTemplate authRedisTemplate) {
        this.BCRYPT_STRENGTH = BCRYPT_STRENGTH;
        this.SESSION_TIME = SESSION_TIME;
        this.userDatabase = userDatabase;
        this.authRedisTemplate = authRedisTemplate;
    }

    LoginResponse login(AuthDto authDto) {
        var user = userDatabase.findUserByUsername(authDto.getUsername());

        if (user.isEmpty() || !bCryptPasswordEncoder.matches(authDto.getPassword(), user.get().getPassword())) {
            throw new AuthorizationException(authDto.getUsername());
        }
        var userToken = UUID.randomUUID();

        var value = AuthRedisDto.builder()
                .userId(user.get().getUserId())
                .role(user.get().getRole())
                .build();

        authRedisTemplate.put(userToken.toString(), value, SESSION_TIME, TimeUnit.MINUTES);
        addAuthenticationTokenToSpringContext(authDto);

        return new LoginResponse(userToken.toString(), user.get().getUserId().toString());
    }

    void logout(UUID token) {
        var isSuccess = authRedisTemplate.delete(token.toString());
        if(isSuccess == null || !isSuccess){
            throw new UserNotLoggedException(token.toString());
        }
    }

    void deactivateUser(UUID userId) {
        var rowsAffected = userDatabase.deactivateUser(userId);
        if( rowsAffected == 0){
            throw new UserNotFoundException(userId);
        }
    }

    void saveUser(UserDto userDto) {
        userDatabase.saveUser(userDto);
    }

    Optional<UserDto> findUserByUsername(String username) {
        return userDatabase.findUserByUsername(username);
    }

    private void addAuthenticationTokenToSpringContext(AuthDto authDto) {
        var dbRoles = userDatabase.getUserRoles(authDto.getUsername());

        var roles = dbRoles.stream()
                .map(roleName -> "ROLE_" + roleName.toUpperCase())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        authDto.getUsername(),
                        authDto.getPassword(),
                        roles
                )
        );
    }
}
