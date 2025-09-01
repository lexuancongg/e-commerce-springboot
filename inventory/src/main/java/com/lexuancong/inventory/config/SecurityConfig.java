package com.lexuancong.inventory.config;

import com.lexuancong.share.constants.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;


@Configuration
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/customer/**").permitAll()
                        .requestMatchers("/management/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }


//    {
//  "exp": 1755877460,
//  "iat": 1755877160,
//  "jti": "57bed55a-f6dd-4ece-9b84-e027f5613592",
//  "iss": "http://localhost:8080/realms/ecommerce",
//  "aud": "account",
//  "sub": "d1d1bab8-ee71-40c5-95ef-9d32818523b0",
//  "typ": "Bearer",
//  "azp": "xuancong-ecommerce",
//  "sid": "653cd956-00ca-47ec-aaa0-b49776596bb3",
//  "acr": "1",
//  "allowed-origins": [
//    "http://host.docker.internal:8000/login/oauth2/code/ecommerce",
//    "http://localhost:8000",
//    "http://localhost:3000"
//  ],
//  "realm_access": {
//    "roles": [
//      "default-roles-ecommerce",
//      "offline_access",
//      "uma_authorization"
//    ]
//  },
//  "resource_access": {
//    "account": {
//      "roles": [
//        "manage-account",
//        "manage-account-links",
//        "view-profile"
//      ]
//    }
//  },
//  "scope": "openid profile email",
//  "email_verified": false,
//  "name": "le cong",
//  "preferred_username": "buff",
//  "given_name": "le",
//  "family_name": "cong",
//  "email": "congle.190904@gmail.com"
//}




    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverterFromKeycloak(){
        Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = jwt -> {
            Map<String,Collection<String>> realmAccess = jwt.getClaim(Constants.KeycloakJwtClaimConstants.REALM_ACCESS);
            Collection<String> roles = realmAccess.get(Constants.KeycloakJwtClaimConstants.ROLES);
            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_".concat(role)))
                    .collect(Collectors.toList());
        };
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;

    }
}