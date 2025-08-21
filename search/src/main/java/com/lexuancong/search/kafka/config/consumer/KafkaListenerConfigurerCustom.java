package com.lexuancong.search.kafka.config.consumer;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListenerConfigurer;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
// KafkaListenerConfigurer : bean custom  kaffka listener container sau khi đc spring tạo bởi ConcurrentKafkaListenerContainerFactory => setup thêm cấu hình, (đầu bếp nấu món ăn xong , phụ bếp decor theem phần sau),
// nó chỉ áp dụng cho ConcurrentKafkaListenerContainerFactory mặc định, hoặc ConcurrentKafkaListenerContainerFactory nào đó được đki trong hàm overight kia
@EnableKafka
@Configuration
public class KafkaListenerConfigurerCustom implements KafkaListenerConfigurer {
    // class có công dụng validator dữ lệu
    private LocalValidatorFactoryBean validator;
    // được tự động nap nhờ <artifactId>spring-boot-starter-validation</artifactId>
    public KafkaListenerConfigurerCustom(LocalValidatorFactoryBean localValidatorFactoryBean) {
        this.validator = localValidatorFactoryBean;
    }

    @Override
    public void configureKafkaListeners(KafkaListenerEndpointRegistrar registrar) {
        // bật tính năng  validator payload của @KafkaListener
        registrar.setValidator(validator);

    }
}


// docs : https://docs.spring.io/spring-kafka/docs/3.3.6/api/org/springframework/kafka/config/package-summary.html
// KafkaListenerEndpointRegistrar :  quản lý và  cấu hình kaffka listenter ,
// Quản l
////Cho phép bạn tùy chỉnh hành vi của listener,
//   KafkaListenerConfigurer = config đăng ký listener (ai, khi nào, cách thêm listener vào hệ thống).
//
//  Còn config hoạt động chi tiết của listener (cách nhận message, xử lý...) nằm ở KafkaListenerContainerFactory.