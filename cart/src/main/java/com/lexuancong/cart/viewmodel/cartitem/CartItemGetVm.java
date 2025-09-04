package com.lexuancong.cart.viewmodel.cartitem;

import com.lexuancong.cart.model.CartItem;
import lombok.Builder;

@Builder
public record CartItemGetVm(String customerId, Long productId, int quantity) {
    public static CartItemGetVm fromModel(CartItem cartItem) {
        return new CartItemGetVm(cartItem.getCustomerId(), cartItem.getProductId(), cartItem.getQuantity());
    }
}
