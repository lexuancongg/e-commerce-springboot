package com.lexuancong.oder.dto.orderitem;

import com.lexuancong.oder.model.Order;
import com.lexuancong.oder.model.OrderItem;
import com.lexuancong.oder.dto.inventory.ProductSubtractQuantity;

import java.math.BigDecimal;

public record OrderItemCreateRequest(
        Long productId,
        int quantity,
        String productName,
        BigDecimal productPrice,
        BigDecimal totalPrice
) {
    public OrderItem toOrderItem(Order order){
        return OrderItem.builder()
                .productId(productId)
                .quantity(quantity)
                .productPrice(productPrice)
                .productName(productName)
                .order(order)
                .build();
    }


    public ProductSubtractQuantity toProductSubtract(){
        return new ProductSubtractQuantity(this.productId(), this.quantity);
    }
}
