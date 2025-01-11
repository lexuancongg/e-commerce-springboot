package com.lexuancong.customer.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakClientConfig {
    private final KeycloakPropsConfig keycloakPropsConfig;
    public KeycloakClientConfig(KeycloakPropsConfig keycloakPropsConfig) {
        this.keycloakPropsConfig = keycloakPropsConfig;
    }
    @Bean
    public Keycloak keycloak(){
        return KeycloakBuilder.builder()
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                // url server keycloak laays token
                .serverUrl(keycloakPropsConfig.getAuthServerUrl())
                // reaml mặc định được thao tác
                .realm(keycloakPropsConfig.getRealm())
                // đại diện id ưứng dụng đăng ký trong keycloak để kết nối
                .clientId(keycloakPropsConfig.getClientId())
                // khóa bị mật đăng ký cùng với clientid trong keycloak để xác thực chống ứng dụng giả mạo
                .clientSecret(keycloakPropsConfig.getCredentials().getSecret())
                .build();
    }



}
