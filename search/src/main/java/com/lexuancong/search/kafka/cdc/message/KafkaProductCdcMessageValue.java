package com.lexuancong.search.kafka.cdc.message;

import com.lexuancong.search.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Getter
@lombok.Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//Debezium : luôn gởi nhwunxg giá key này vào kafka
public class KafkaProductCdcMessageValue {
    private Operation op;
    private Product after;
    private Product before;
    private Object source;
    private  String ts_ms;
    private Object transaction;
}
