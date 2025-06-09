package com.lexuancong.search.kafka.consumer;

import com.lexuancong.search.kafka.cdc.message.KafkaProductCdcMessageValue;
import com.lexuancong.search.kafka.cdc.message.KafkaProductMsgKey;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

// cập nhật lại product trong eletisearch
public class ProductSyncDataConsumer {
    @KafkaListener(
            id = "product-sync-elastic",
            topics ="product",
            // hoạt đọng dựa vào factory này
            containerFactory ="productCdcListenerContainerFactory",
            // dùng làm load balance  , nếu nhận 100 messgae mà có hai consumer , thì chia mỗi cái 50
            groupId ="product-sync"
    )
    public void processMessage(
            @Header(KafkaHeaders.RECEIVED_KEY) KafkaProductMsgKey key,
            @Payload(required = false)KafkaProductCdcMessageValue kafkaProductCdcMessageValue,
            @Headers MessageHeaders headers
            ) {


    }
}
