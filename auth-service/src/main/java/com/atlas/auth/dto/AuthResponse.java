package com.atlas.auth.dto;

import com.atlas.auth.entity.Role;

public record AuthResponse(
        String accessToken,
        String tokenType,
        String email,
        Role role
) {
}
