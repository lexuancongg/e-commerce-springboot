package com.lexuancong.cart.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "xuancong.service")
public record ServiceUrlConfig (String product){
}
