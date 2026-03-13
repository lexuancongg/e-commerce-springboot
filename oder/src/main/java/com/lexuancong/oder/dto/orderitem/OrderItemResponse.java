package com.lexuancong.oder.dto.orderitem;

import com.lexuancong.oder.model.OrderItem;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long id,
        Long productId,
        int quantity,
        BigDecimal productPrice,
        Long orderId,
        String productName

) {
    public static OrderItemResponse fromOrderItem(OrderItem orderItem) {
        return new OrderItemResponse(orderItem.getId(),
                orderItem.getProductId(),orderItem.getQuantity(),orderItem.getProductPrice(),
                orderItem.getProductId(),orderItem.getProductName()
        );

    }
}
