package com.lexuancong.search.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class ElasticsearchConfig {
    private String hostAndPort;
    private String username;
    private String password;


}
