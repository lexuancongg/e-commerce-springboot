package com.lexuancong.search.config.kafka.consumer;

import com.lexuancong.search.config.kafka.cdc.message.KafkaProductMsgKey;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

public class ProductSyncDataConsumer {
    @KafkaListener(
            id = "product-sync-elastic",
            topics ="product",
            containerFactory ="productCdcListenerContainerFactory"
    )
    public void processMessage(
            @Header(KafkaHeaders.RECEIVED_KEY)KafkaProductMsgKey key
            ) {

    }
}
