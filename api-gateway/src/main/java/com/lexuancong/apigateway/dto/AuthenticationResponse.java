package com.lexuancong.apigateway.dto;

public record AuthenticationResponse(
        boolean isAuthenticated,
        AuthenticatedUserResponse authenticatedUser
) {
}
