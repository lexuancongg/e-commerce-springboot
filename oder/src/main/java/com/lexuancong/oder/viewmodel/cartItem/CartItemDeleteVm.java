package com.lexuancong.oder.viewmodel.cartItem;

import com.lexuancong.oder.model.OrderItem;

public record CartItemDeleteVm(Long productId, int quantity) {
    public static CartItemDeleteVm fromOderItem(OrderItem orderItem) {
        return new CartItemDeleteVm(orderItem.getProductId(), orderItem.getQuantity());
    }
}
