package com.wspa.auth;

import com.wspa.auth.exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
@AllArgsConstructor
class UserDatabase {
    private final JdbcTemplate template;
    private final PasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);

    Optional<UserDto> findUserByUsername(String username) {
        var sql = """
                SELECT 
                user_id as "user_id", 
                login as "login", 
                password as "password", 
                type as "type", 
                active as "active"
                FROM users
                WHERE login = ?
                """;
        return template.query(sql,
                        ps -> ps.setString(1, username),
                        (rs, row) -> UserDto.builder()
                                .userId((UUID) rs.getObject("user_id"))
                                .username(rs.getString("login"))
                                .password(rs.getString("password"))
                                .active(rs.getBoolean("active"))
                                .build())
                .stream()
                .findFirst();
    }

    int deactivateUser(UUID userId) {

        var sql = """
                Update users
                set active = false
                where user_id = ?;
                """;

        return template.update(sql,
                ps -> ps.setString(1, userId.toString()));
    }

    Set<String> getUserRoles(String username) {
        var sql = """
                select type 
                from users 
                where login = ?;
                """;
        var roles = template.query(sql,
                ps -> ps.setString(1, username),
                (rs, row) -> rs.getString(1));
        if (roles.size() == 0) {
            throw new UserNotFoundException();
        }
        return Set.of(roles.get(0).split(","));
    }

    void saveUser(UserDto userDto) {
        final var sql = """
                INSERT INTO users(user_id, login, password, type, active)
                VALUES (?, ?, ?, ?, ?);
                """;

        template.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, userDto.getUserId(), java.sql.Types.OTHER);
            ps.setString(2, userDto.getUsername());
            ps.setString(3, bCryptPasswordEncoder.encode(userDto.getPassword()));
            ps.setBoolean(5, true);
            return ps;
        });
    }
}
