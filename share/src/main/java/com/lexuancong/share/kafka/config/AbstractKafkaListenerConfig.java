package com.lexuancong.share.kafka.config;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

// config kafka => chủ yếu config cách cho containerfactory để tạo ra container cho các listener dùng để hoạt động

public abstract class  AbstractKafkaListenerConfig <K,V>{
    private final Class<K> keyType;
    private final Class<V> valueType;
    private final KafkaProperties kafkaProperties;

    // vì  trong runtime generic bị xóa bỏ nên lưu lại
    // KafkaProperties được inject vào từ bean do mapper từ file properties
    public AbstractKafkaListenerConfig(Class<K> keyType, Class<V> valueType, KafkaProperties kafkaProperties) {
        this.keyType = keyType;
        this.valueType = valueType;
        this.kafkaProperties = kafkaProperties;
    }


    public abstract ConcurrentKafkaListenerContainerFactory<K,V> listenerContainerFactory();


    public  ConcurrentKafkaListenerContainerFactory<K,V> kafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<K,V> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(this.typeConsumerFactory(keyType, valueType));
        return factory;
    }


//Consumer là cái thực sự nhận message từ Kafka topic.
//Nó biết cách connect Kafka, deserialize key/value, group id, v.v…
//ConsumerFactory làm gì?
//consumerFactory tạo ra các consumer instance theo config bạn định nghĩa.

    private ConsumerFactory<K, V> typeConsumerFactory(Class<K> keyClazz, Class<V> valueClazz) {
        Map<String, Object> props = buildConsumerProperties();

        // ErrorHandlingDeserializer : khi kafka send message json sai format => JsonDeserializer=> ném lỗi => app crash và listener bị dung
        // => nos bọc lôĩ vào DeserializationException => cho phép :
//        1.Gửi message lỗi sang Dead Letter Topic (DLT)
//        2.Bỏ qua message lỗi mà vẫn tiếp tục xử lý các message khác hoặc log lỗi
        var keyDeserialize = new ErrorHandlingDeserializer<>(getJsonDeserializer(keyClazz));
        var valueDeserialize = new ErrorHandlingDeserializer<>(getJsonDeserializer(valueClazz));
        return new DefaultKafkaConsumerFactory<>(props, keyDeserialize, valueDeserialize);
    }

    private static <T> JsonDeserializer<T> getJsonDeserializer(Class<T> clazz) {
        var jsonDeserializer = new JsonDeserializer<>(clazz);
        jsonDeserializer.addTrustedPackages("*");
        return jsonDeserializer;
    }

    private Map<String, Object> buildConsumerProperties() {
        return kafkaProperties.buildConsumerProperties(null);
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
