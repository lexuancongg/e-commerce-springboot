package com.lexuancong.search.kafka.config.consumer;

import com.lexuancong.search.kafka.cdc.message.KafkaProductCdcMessageValue;
import com.lexuancong.search.kafka.cdc.message.KafkaProductMessageKey;
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
// config khi dữ liệu product thay đổi => cập nhật lại
// config kafka => chủ yếu config cách cho containerfactory để tạo ra container cho các listener dùng để hoạt động
public class ProductCdcKafkaListenerConfig {

    // KafkaProperties được inject vào từ bean do mapper từ file properties
    @Bean(name = "productCdcListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<KafkaProductMessageKey, KafkaProductCdcMessageValue> kafkaListenerContainerFactory(KafkaProperties properties) {
        var factory = new ConcurrentKafkaListenerContainerFactory<KafkaProductMessageKey, KafkaProductCdcMessageValue>();
        factory.setConsumerFactory(this.consumerFactory(KafkaProductMessageKey.class, KafkaProductCdcMessageValue.class, properties));
        return factory;
    }

//Consumer là cái thực sự nhận message từ Kafka topic.
//Nó biết cách connect Kafka, deserialize key/value, group id, v.v…
//ConsumerFactory làm gì?
//consumerFactory tạo ra các consumer instance theo config bạn định nghĩa.
    private ConsumerFactory<KafkaProductMessageKey, KafkaProductCdcMessageValue> consumerFactory(Class<KafkaProductMessageKey> keyclass,
                                                                                                 Class<KafkaProductCdcMessageValue> valueclass,
                                                                                                 KafkaProperties kafkaProperties) {
        Map<String, Object> props = this.buildConsumerProperties(kafkaProperties);
        // ErrorHandlingDeserializer : khi kafka send message json sai format => JsonDeserializer=> ném lỗi => app crash và listener bị dung
        // => nos bọc lôĩ vào DeserializationException => cho phép :
//        1.Gửi message lỗi sang Dead Letter Topic (DLT)
//        2.Bỏ qua message lỗi mà vẫn tiếp tục xử lý các message khác hoặc log lỗi
        var keyDeserialize = new ErrorHandlingDeserializer<>(this.gettJsonDeserializer(keyclass));
        var valueDeserialize = new ErrorHandlingDeserializer<>(this.gettJsonDeserializer(valueclass));
        return new DefaultKafkaConsumerFactory<>(props, keyDeserialize, valueDeserialize);
    }

    private Map<String, Object> buildConsumerProperties(KafkaProperties kafkaProperties) {
        // không custom clientId
        return kafkaProperties.buildConsumerProperties(null);
    }

    private <T> JsonDeserializer<T> gettJsonDeserializer(Class<T> tClass) {
        var jsonDeserializer = new JsonDeserializer<>(tClass);
        // deserialize  : cho phép deserialize  tất cả các class ở các package
        jsonDeserializer.addTrustedPackages("*");
        return jsonDeserializer;
    }

}

// doc

//ConcurrentKafkaListenerContainerFactory là bean dùng để cấu hình cách mà @KafkaListener hoạt động. Nó là công thức tạo ra các “ Kafka listener container ”  theo config để hoạt động như lắng nghe message từ Kafka.
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