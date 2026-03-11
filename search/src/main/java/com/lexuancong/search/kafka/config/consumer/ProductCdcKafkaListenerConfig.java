package com.lexuancong.search.kafka.config.consumer;

import com.lexuancong.search.kafka.cdc.message.KafkaProductCdcMessageValue;
import com.lexuancong.search.kafka.cdc.message.KafkaProductMessageKey;
import com.lexuancong.share.kafka.config.AbstractKafkaListenerConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@EnableKafka
@Configuration
// config kafka => chủ yếu config cách cho containerfactory để tạo ra container cho các listener dùng để hoạt động
public class ProductCdcKafkaListenerConfig extends AbstractKafkaListenerConfig<KafkaProductMessageKey,KafkaProductCdcMessageValue> {



    public ProductCdcKafkaListenerConfig(KafkaProperties kafkaProperties) {
        super(KafkaProductMessageKey.class, KafkaProductCdcMessageValue.class, kafkaProperties);
    }

    @Bean
    @Override
    public ConcurrentKafkaListenerContainerFactory<KafkaProductMessageKey, KafkaProductCdcMessageValue> listenerContainerFactory() {
        return this.kafkaListenerContainerFactory();
    }
}
