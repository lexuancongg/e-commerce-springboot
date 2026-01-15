package com.lexuancong.cart.dto.cartitem;

import com.lexuancong.cart.model.CartItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemCreateRequest(@NotNull Long productId, @NotNull @Min(1) int quantity) {

    public CartItem toCartItem(String customerId){

        return new CartItem(customerId, productId, quantity);
    }
}
