package com.wspa.auth;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@Data
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
class UserDto implements Serializable {
    private Long id;
    @NonNull
    private UUID userId;
    @NonNull
    private String username;
    @NonNull
    private String password;
    private Boolean active;
    private AppUserRole role;
}
