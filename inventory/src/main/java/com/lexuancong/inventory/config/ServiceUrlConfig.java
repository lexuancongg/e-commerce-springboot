package com.lexuancong.inventory.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "xuancong.service")
public record ServiceUrlConfig(String product, String address) {
}
