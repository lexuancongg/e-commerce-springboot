package com.lexuancong.customer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakPropsConfig {
    private  String authServerUrl;
    private String realm;
    private String clientId;
    private Credentials credentials = new Credentials();

    @Getter
    @Setter
    public class Credentials {
        private String secret;
    }
}
