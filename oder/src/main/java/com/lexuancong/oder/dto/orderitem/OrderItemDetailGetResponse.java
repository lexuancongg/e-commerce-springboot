package com.lexuancong.oder.dto.orderitem;

import com.lexuancong.oder.model.OrderItem;

import java.math.BigDecimal;

public record OrderItemDetailGetResponse(
        Long id,
        Long productId,
        String productName,
        int quantity,
        BigDecimal productPrice ,
        BigDecimal totalPrice
) {
    public static OrderItemDetailGetResponse fromModel(OrderItem orderItem) {
        return new OrderItemDetailGetResponse(orderItem.getId(),orderItem.getProductId(),orderItem.getProductName(),
                orderItem.getQuantity(),orderItem.getProductPrice(),orderItem.getTotalPrice());
    }
}
