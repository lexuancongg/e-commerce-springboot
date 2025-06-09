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
// phải biết đc action , thông tin sp sau thay đổi , sản phẩm trước khi bị thay đổi như edit
public class KafkaProductCdcMessageValue {
    private Operation operation;
    private Product productBefore;
    private Product productAfter;
}
