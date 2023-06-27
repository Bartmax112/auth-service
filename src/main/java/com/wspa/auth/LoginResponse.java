package com.wspa.auth;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
class LoginResponse {
    private final String token;
    private final String userId;
}
