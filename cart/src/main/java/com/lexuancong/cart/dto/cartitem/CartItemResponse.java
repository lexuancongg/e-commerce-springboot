package com.lexuancong.cart.dto.cartitem;

import com.lexuancong.cart.model.CartItem;
import lombok.Builder;

@Builder
public record CartItemResponse(String customerId, Long productId, int quantity) {
    public static CartItemResponse fromCartItem(CartItem cartItem) {
        return new CartItemResponse(
                cartItem.getCustomerId(),
                cartItem.getProductId(),
                cartItem.getQuantity());
    }

}
