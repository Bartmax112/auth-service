package com.wspa.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
class UserController {
    private final UserService userService;

    @PostMapping("/login")
    ResponseEntity<String> login(@RequestBody AuthDto authDto) {
        userService.login(authDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/logout")
    void logout(@RequestHeader(AUTHORIZATION) String token) {
        userService.logout(UUID.fromString(token));
    }

    @PreAuthorize("hasAnyRole('CASHIER')")
    @PutMapping
    void deactivateUser(@RequestHeader(AUTHORIZATION) UUID userId) {
        userService.deactivateUser(userId);
    }
}