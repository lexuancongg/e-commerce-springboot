package com.lexuancong.oder.viewmodel.orderitem;

import com.lexuancong.oder.model.OrderItem;

import java.math.BigDecimal;

public record OrderItemVm(
        Long id,
        Long productId,
        int quantity,
        BigDecimal productPrice,
        BigDecimal totalPrice,
        Long orderId

) {
    public static OrderItemVm fromModel(OrderItem orderItem) {
        return new OrderItemVm(orderItem.getId(),
                orderItem.getProductId(),orderItem.getQuantity(),orderItem.getProductPrice(),
                orderItem.getTotalPrice(),orderItem.getProductId());

    }
}
