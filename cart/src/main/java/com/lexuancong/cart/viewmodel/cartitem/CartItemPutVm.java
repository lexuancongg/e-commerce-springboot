package com.lexuancong.cart.viewmodel.cartitem;

import com.lexuancong.cart.model.CartItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemPutVm(@NotNull @Min(1) int quantity) {

    public  CartItem toModel(String customerId, Long productId){
        return  CartItem.builder()
                .customerId(customerId)
                .productId(productId)
                .quantity(this.quantity)
                .build();
    }
}
