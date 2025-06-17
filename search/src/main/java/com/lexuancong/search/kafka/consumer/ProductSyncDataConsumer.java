package com.lexuancong.search.kafka.consumer;

import com.lexuancong.search.kafka.cdc.message.KafkaProductCdcMessageValue;
import com.lexuancong.search.kafka.cdc.message.KafkaProductMessageKey;
import com.lexuancong.search.kafka.cdc.message.Operation;
import com.lexuancong.search.service.ProductSyncDataElasticsearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.function.BiConsumer;

// cập nhật lại product trong eletisearch : debezium => track(theo dõi thay ổi db) => bắn event vào kafkaf => kafka lấy dữ liệu (source) => kafka đưa dữ liệu ra ngoài (es) (sink) => es cập nhật vào
@Service
@RequiredArgsConstructor
public class ProductSyncDataConsumer {
    private  final ProductSyncDataElasticsearchService productSyncDataElasticsearchService;

    @KafkaListener(
            id = "product-cdc-elastic-listener",
            topics ="xuancong-commerce.public.product",
            // hoạt đọng dựa vào factory này
            containerFactory ="productCdcListenerContainerFactory",
            // dùng làm load balance  , nếu nhận 100 messgae mà có hai consumer , thì chia mỗi cái 50
            groupId ="product-sync-group"
    )
    public void processMessage(
            @Header(KafkaHeaders.RECEIVED_KEY) KafkaProductMessageKey kafkaProductMessageKey,
            @Payload(required = false)KafkaProductCdcMessageValue kafkaProductCdcMessageValue,
            @Headers MessageHeaders headers
            ) {
        this.processProductCdcEvent(kafkaProductMessageKey,kafkaProductCdcMessageValue,this::syncProducts);

    }

    public void syncProducts(KafkaProductMessageKey kafkaProductMessageKey, KafkaProductCdcMessageValue kafkaProductCdcMessageValue){
        // xác định action
        Operation operation = kafkaProductCdcMessageValue.getOp();
        switch (operation){
            case CREATE -> this.productSyncDataElasticsearchService.createProductSyncData(kafkaProductMessageKey.getId(),
                    kafkaProductCdcMessageValue.getAfter());
            case DELETE -> this.productSyncDataElasticsearchService.deleteProductSyncData(kafkaProductMessageKey.getId());
            case UPDATE -> this.productSyncDataElasticsearchService.updateProductSyncData(kafkaProductMessageKey.getId() ,
                    kafkaProductCdcMessageValue.getAfter());
        }
    }

    public void processProductCdcEvent(KafkaProductMessageKey kafkaProductMessageKey, KafkaProductCdcMessageValue kafkaProductCdcMessageValue,
                                       BiConsumer<KafkaProductMessageKey,KafkaProductCdcMessageValue>  biConsumer){
        biConsumer.accept(kafkaProductMessageKey,kafkaProductCdcMessageValue);
    }
}
