package com.lexuancong.oder.dto.orderitem;

import com.lexuancong.oder.model.OrderItem;

import java.math.BigDecimal;

public record OrderItemGetResponse(
        Long id,
        Long productId,
        int quantity,
        BigDecimal productPrice,
        BigDecimal totalPrice,
        Long orderId,
        String productName

) {
    public static OrderItemGetResponse fromModel(OrderItem orderItem) {
        return new OrderItemGetResponse(orderItem.getId(),
                orderItem.getProductId(),orderItem.getQuantity(),orderItem.getProductPrice(),
                orderItem.getTotalPrice(),orderItem.getProductId(),orderItem.getProductName()
        );

    }
}
