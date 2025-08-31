package com.lexuancong.oder.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "xuancong.service")
public record ServiceUrlConfig(
        String cart,
        String product
) {
}
