package com.lexuancong.cart.viewmodel.cartitem;

import com.lexuancong.cart.model.CartItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemPostVm(@NotNull Long productId, @NotNull @Min(1) int quantity) {

    public CartItem toModel(String customerId){
        return new CartItem(customerId, productId, quantity);
    }
}
