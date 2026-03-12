package com.lexuancong.feedback.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "xuancong.service")
public record ServiceUrlsProperties(String product, String customer, String order) {
}
