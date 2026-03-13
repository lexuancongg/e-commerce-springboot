package com.lexuancong.oder.kafka.config.consumer;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListenerConfigurer;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@EnableKafka
@Configuration
public class KafkaListenerConfigurerCustom implements KafkaListenerConfigurer {
    private LocalValidatorFactoryBean validator;
    public KafkaListenerConfigurerCustom(LocalValidatorFactoryBean localValidatorFactoryBean) {
        this.validator = localValidatorFactoryBean;
    }

    @Override
    public void configureKafkaListeners(KafkaListenerEndpointRegistrar registrar) {
        registrar.setValidator(validator);

    }
}

