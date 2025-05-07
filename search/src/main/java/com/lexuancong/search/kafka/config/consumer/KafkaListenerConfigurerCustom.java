package com.lexuancong.search.kafka.config.consumer;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListenerConfigurer;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

// KafkaListenerConfigurer : bean custom cách kaffka handle listener
@EnableKafka
@Configuration
public class KafkaListenerConfigurerCustom implements KafkaListenerConfigurer {
    // class có công dụng validator dữ lệu
    private LocalValidatorFactoryBean validator;
    // được tự động nap nhờ
//			<artifactId>spring-boot-starter-validation</artifactId>
    public KafkaListenerConfigurerCustom(LocalValidatorFactoryBean localValidatorFactoryBean) {
        this.validator = localValidatorFactoryBean;
    }

    @Override
    public void configureKafkaListeners(KafkaListenerEndpointRegistrar registrar) {
        // bật tính năng  validator payload của @KafkaListener
        registrar.setValidator(validator);

    }
}
