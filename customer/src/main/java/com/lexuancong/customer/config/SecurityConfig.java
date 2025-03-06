package com.lexuancong.customer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfig {
    // bean đĩnh nghĩa chuổi bộ lọc filter cho request đi qua
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // caaus hình quyền truy cập http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/storefront/**").permitAll()
                        .requestMatchers("/backoffice/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }


    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverterForKeycloak() {
        Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = jwt ->{
            Map<String,Collection<String>> realmAccess = jwt.getClaim("realmAccess");
            Collection<String> roles = realmAccess.get("roles");
            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_".concat(role)))
                    .collect(Collectors.toList());
        };
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;

    }
//    @Bean
//    public JwtDecoder jwtDecoder() {
//        String issuerUri = "http://localhost:8080/realms/xuancong-ecommerce"; // Thay bằng issuer của bạn
//
//        // đôi tượng giải max và check tính hợp lệ token
//        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(issuerUri + "/protocol/openid-connect/certs").build();
//
//        return new JwtDecoder() {
//            @Override
//            public Jwt decode(String token) throws JwtException {
//                try {
//                    return jwtDecoder.decode(token);
//                } catch (JwtException e) {
//                    System.err.println("Lỗi giải mã JWT: " + e.getMessage()); // Ghi log lỗi
//                    throw e; // Ném lỗi để Spring xử lý
//                }
//            }
//        };
//    }


}
