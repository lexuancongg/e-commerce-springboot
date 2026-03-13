package com.lexuancong.oder.kafka.message;

import com.lexuancong.oder.dto.inventory.ProductSubtractQuantity;
import lombok.Builder;

import java.util.List;
@Builder
public record  OrderCreatedMessage(
        String customerId,
        List<ProductSubtractQuantity> productSubtract
) {

}




