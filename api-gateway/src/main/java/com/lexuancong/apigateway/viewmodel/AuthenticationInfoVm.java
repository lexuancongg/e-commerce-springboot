package com.lexuancong.apigateway.viewmodel;

public record AuthenticationInfoVm(
        boolean isAuthenticated,
        AuthenticatedUserVm authenticatedUser
) {
}
