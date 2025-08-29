package com.lexuancong.product.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "xuancong.service")
public record ServiceUrlConfig(String image , String  product) {
}
