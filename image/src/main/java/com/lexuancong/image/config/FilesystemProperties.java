package com.lexuancong.image.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class FilesystemProperties {

    @Value("${filesystem.directory}")
    private String directory;

}