package com.lexuancong.oder.viewmodel.orderitem;

import com.lexuancong.oder.model.Order;
import com.lexuancong.oder.model.OrderItem;

import java.math.BigDecimal;

public record OrderItemPostVm(
        Long productId,
        int quantity,
        String productName,
        BigDecimal productPrice,
        BigDecimal totalPrice
) {
    public OrderItem toModel(Order order){
        return OrderItem.builder()
                .productId(productId)
                .quantity(quantity)
                .productPrice(productPrice)
                .totalPrice(totalPrice)
                .productName(productName)
                .order(order)
                .oderId(order.getId())
                .build();
    }
}
