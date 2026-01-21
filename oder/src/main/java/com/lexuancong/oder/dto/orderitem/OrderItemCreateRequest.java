package com.lexuancong.oder.dto.orderitem;

import com.lexuancong.oder.model.Order;
import com.lexuancong.oder.model.OrderItem;
import com.lexuancong.oder.dto.inventory.InventorySubtract;

import java.math.BigDecimal;

public record OrderItemCreateRequest(
        Long productId,
        int quantity,
        String productName,
        BigDecimal productPrice,
        BigDecimal totalPrice
) {
    public OrderItem toOrder(Order order){
        return OrderItem.builder()
                .productId(productId)
                .quantity(quantity)
                .productPrice(productPrice)
                .totalPrice(totalPrice)
                .productName(productName)
                .order(order)
                .build();
    }


    public InventorySubtract toInventorySubtract(){
        return new InventorySubtract(this.productId(), this.quantity);
    }
}
