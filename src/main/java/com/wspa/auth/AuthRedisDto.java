package com.wspa.auth;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
class AuthRedisDto {
    private UUID userId;
    private AppUserRole role;
}
