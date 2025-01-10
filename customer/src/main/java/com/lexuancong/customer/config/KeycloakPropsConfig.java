package com.lexuancong.customer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class KeycloakPropsConfig {
    private String authServerUrl;
    private String realm;
    private String resource;
    private Credentials credentials = new Credentials();
    public KeycloakPropsConfig() {
        System.out.printf("calll");
    }
    @Getter
    @Setter
    public class Credentials {
        private String secret;
    }
}
