package com.lexuancong.cart.kafka.config.cusomer;

import com.lexuancong.cart.kafka.message.OrderCreatedMessage;
import com.lexuancong.share.kafka.config.AbstractKafkaListenerConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
@EnableKafka
@Configuration
public class KafkaOrderCreatedListenerConfig extends AbstractKafkaListenerConfig<String, OrderCreatedMessage> {
    public KafkaOrderCreatedListenerConfig(KafkaProperties kafkaProperties) {
        super(String.class, OrderCreatedMessage.class, kafkaProperties);
    }


    @Bean(name = "orderCreatedListenerFactory")
    @Override
    public ConcurrentKafkaListenerContainerFactory<String,OrderCreatedMessage> listenerContainerFactory() {
        return super.kafkaListenerContainerFactory();
    }
}
