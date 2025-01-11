package com.lexuancong.customer.utils;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class AuthenticationUtils {

    public static String extractCustomerIdFromJwt() {
        Authentication authentication = getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            // bắn ra ngoại leej
        }
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        return jwtAuthenticationToken.getToken().getSubject();
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static String extractJwt() {
        return ((Jwt) getAuthentication().getPrincipal()).getTokenValue();
    }


}
