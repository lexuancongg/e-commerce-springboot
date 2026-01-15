package com.lexuancong.apigateway.dto;

public record AuthenticationGetResponse(
        boolean isAuthenticated,
        AuthenticatedUserResponse authenticatedUser
) {
}
