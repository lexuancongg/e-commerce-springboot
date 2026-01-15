package com.lexuancong.oder.viewmodel.product;

import com.lexuancong.oder.model.OrderItem;

public record ProductSubtractQuantityVm(Long productId, int quantity) {
    public static ProductSubtractQuantityVm fromOrderItem(OrderItem orderItem) {
        return new ProductSubtractQuantityVm(orderItem.getProductId(), orderItem.getQuantity());
    }
}

