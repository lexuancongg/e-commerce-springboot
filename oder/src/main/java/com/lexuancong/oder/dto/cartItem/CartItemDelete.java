package com.lexuancong.oder.dto.cartItem;

import com.lexuancong.oder.model.OrderItem;

public record CartItemDelete(Long productId, int quantity) {
    public static CartItemDelete fromOderItem(OrderItem orderItem) {
        return new CartItemDelete(orderItem.getProductId(), orderItem.getQuantity());
    }
}
