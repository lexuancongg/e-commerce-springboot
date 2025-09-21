package com.lexuancong.cart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class EntityAuditConfig {
    @Bean
    // auditting trong JPa
    public AuditorAware<String> auditorAware(){
        return ()->{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication == null || !authentication.isAuthenticated() || authentication.getName().equals("anonymousUser")){
                return Optional.of("");
            }
            return Optional.of(authentication.getName());
        };

    }
}
