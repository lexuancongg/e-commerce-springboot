package com.lexuancong.oder;

import com.lexuancong.oder.config.ServiceUrlConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ServiceUrlConfig.class})
public class OderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OderApplication.class, args);
    }

}
