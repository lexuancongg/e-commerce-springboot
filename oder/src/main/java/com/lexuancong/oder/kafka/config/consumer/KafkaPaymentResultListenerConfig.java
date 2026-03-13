package com.lexuancong.oder.kafka.config.consumer;

import com.lexuancong.oder.kafka.message.PaymentResultMessage;
import com.lexuancong.share.kafka.config.AbstractKafkaListenerConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
@Configuration
@EnableKafka
public class KafkaPaymentResultListenerConfig extends AbstractKafkaListenerConfig<String, PaymentResultMessage> {
    public KafkaPaymentResultListenerConfig(KafkaProperties kafkaProperties) {
        super(String.class, PaymentResultMessage.class, kafkaProperties);
    }
    @Bean(name = "kafkaPaymentResultContainerFactory")
    @Override
    public ConcurrentKafkaListenerContainerFactory<String, PaymentResultMessage> listenerContainerFactory() {
        return super.kafkaListenerContainerFactory();
    }
}
