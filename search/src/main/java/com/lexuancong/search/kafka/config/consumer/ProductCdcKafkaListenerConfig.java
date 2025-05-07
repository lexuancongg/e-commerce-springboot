package com.lexuancong.search.kafka.config.consumer;

import com.lexuancong.search.kafka.cdc.message.KafkaProductCdcMessageValue;
import com.lexuancong.search.kafka.cdc.message.KafkaProductMsgKey;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@EnableKafka
@Configuration
public class ProductCdcKafkaListenerConfig {

    // KafkaProperties được kafka inject cho
    @Bean
    public ConcurrentKafkaListenerContainerFactory<KafkaProductMsgKey, KafkaProductCdcMessageValue> kafkaListenerContainerFactory(KafkaProperties properties) {
        var factory = new ConcurrentKafkaListenerContainerFactory<KafkaProductMsgKey,KafkaProductCdcMessageValue>();
        factory.setConsumerFactory(this.consumerFactory(KafkaProductMsgKey.class, KafkaProductCdcMessageValue.class, properties));
        return factory;
    }

    private ConsumerFactory<KafkaProductMsgKey,KafkaProductCdcMessageValue> consumerFactory(Class<KafkaProductMsgKey>  keyclass ,
                                                                                            Class<KafkaProductCdcMessageValue> valueclass ,
                                                                                            KafkaProperties kafkaProperties) {
        Map<String, Object> props = this.buildConsumerProperties(kafkaProperties);
        var keyDeserialize = new ErrorHandlingDeserializer<>(gettJsonDeserializer(keyclass));
        var valueDeserialize = new ErrorHandlingDeserializer<>(gettJsonDeserializer(valueclass));
        return new DefaultKafkaConsumerFactory<>(props, keyDeserialize, valueDeserialize);

    }

    private Map<String, Object> buildConsumerProperties(KafkaProperties kafkaProperties) {
        return kafkaProperties.buildConsumerProperties(null);
    }

    private <T>JsonDeserializer<T> gettJsonDeserializer(Class<T> tClass){
        var jsonDeserializer = new JsonDeserializer<>(tClass);
        jsonDeserializer.addTrustedPackages("*");
        return jsonDeserializer;
    }


}

// doc


//ConcurrentKafkaListenerContainerFactory là bean dùng để cấu hình cách mà @KafkaListener hoạt động. Nó tạo ra các “container” để lắng nghe message từ Kafka.
//
//  Công dụng chính:
//Thiết lập:
//
//ConsumerFactory (cách deserialize message)
//
//Số lượng thread concurrent (setConcurrency)
//
//Cách xử lý lỗi (setErrorHandler)
//
//Manual commit offset, batch listener, retry,...
//
//Là factory nên nó tạo ra các container thực sự chạy ngầm để tiêu thụ message.
//
//
//
//
//setConsumerFactory(...) trong ConcurrentKafkaListenerContainerFactory dùng để chỉ định cách tạo Kafka consumer thực sự, tức là nói cho Spring biết:
//
//        “Khi nào tạo consumer để lắng nghe message, thì dùng cái factory này để config cách deserialize, groupId, auto-offset,...”
//→ setConsumerFactory(...) chính là cái "cầu nối" giữa Spring và Kafka client lib. Nếu không set, thì listener sẽ không biết tạo consumer kiểu gì.




// factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(props));
//Ở đây props có thể chứa:
//
//java
//Copy
//Edit
//props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
//props.put(ConsumerConfig.GROUP_ID_CONFIG, "product-group");