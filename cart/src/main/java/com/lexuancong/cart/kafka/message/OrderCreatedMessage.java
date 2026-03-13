package com.lexuancong.cart.kafka.message;

import com.lexuancong.cart.dto.cartitem.ProductSubtractQuantity;
import lombok.Builder;

import java.util.List;

@Builder
public record OrderCreatedMessage(
        String customerId,
        List<ProductSubtractQuantity> productSubtract
) {

}




