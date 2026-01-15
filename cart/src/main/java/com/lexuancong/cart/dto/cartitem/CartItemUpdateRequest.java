package com.lexuancong.cart.dto.cartitem;

import com.lexuancong.cart.model.CartItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemUpdateRequest(@NotNull @Min(1) int quantity) {

    public  CartItem toCartItem(String customerId, Long productId){
        return  CartItem.builder()
                .customerId(customerId)
                .productId(productId)
                .quantity(this.quantity)
                .build();
    }
}
