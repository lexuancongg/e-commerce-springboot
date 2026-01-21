package com.lexuancong.oder.dto.checkoutitem;

import com.lexuancong.oder.model.CheckoutItem;

public record CheckoutItemGetResponse(
        Long id,
        Long productId,
        int quantity
){
    public static CheckoutItemGetResponse fromCheckoutItem(CheckoutItem checkoutItem) {
        return new CheckoutItemGetResponse(
                checkoutItem.getId() ,
                checkoutItem.getProductId(),
                checkoutItem.getQuantity()
        );
    }
}
