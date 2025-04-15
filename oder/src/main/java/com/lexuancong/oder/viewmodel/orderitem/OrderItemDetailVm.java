package com.lexuancong.oder.viewmodel.orderitem;

import com.lexuancong.oder.model.OrderItem;

import java.math.BigDecimal;

public record OrderItemDetailVm(
        Long id,
        Long productId,
        String productName,
        int quantity,
        BigDecimal productPrice ,
        BigDecimal totalPrice
) {
    public static OrderItemDetailVm fromModel(OrderItem orderItem) {
        return new OrderItemDetailVm(orderItem.getId(),orderItem.getProductId(),orderItem.getProductName(),
                orderItem.getQuantity(),orderItem.getProductPrice(),orderItem.getTotalPrice());
    }
}
