package com.lexuancong.cart.dto.cartitem;

import com.lexuancong.cart.model.CartItem;
import lombok.Builder;

@Builder
public record CartItemGetResponse(String customerId, Long productId, int quantity) {
    public static CartItemGetResponse fromCartItem(CartItem cartItem) {
        return new CartItemGetResponse(cartItem.getCustomerId(), cartItem.getProductId(), cartItem.getQuantity());
    }

}
