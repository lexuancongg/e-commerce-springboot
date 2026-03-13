package com.lexuancong.oder.dto.checkoutitem;

import com.lexuancong.oder.model.CheckoutItem;

public record CheckoutItemResponse(
        Long id,
        Long productId,
        int quantity
){
    public static CheckoutItemResponse fromCheckoutItem(CheckoutItem checkoutItem) {
        return new CheckoutItemResponse(
                checkoutItem.getId() ,
                checkoutItem.getProductId(),
                checkoutItem.getQuantity()
        );
    }
}
