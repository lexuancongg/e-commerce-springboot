package com.lexuancong.oder.dto.product;

import com.lexuancong.oder.model.OrderItem;

public record ProductSubtractQuantity(Long productId, int quantity) {
    public static ProductSubtractQuantity fromOrderItem(OrderItem orderItem) {
        return new ProductSubtractQuantity(orderItem.getProductId(), orderItem.getQuantity());
    }
}

