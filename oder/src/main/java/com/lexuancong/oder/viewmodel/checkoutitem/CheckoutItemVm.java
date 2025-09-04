package com.lexuancong.oder.viewmodel.checkoutitem;

import com.lexuancong.oder.model.CheckoutItem;

public record CheckoutItemVm (
        Long id,
        Long productId,
        int quantity
){
    public static CheckoutItemVm fromModel(CheckoutItem checkoutItem) {
        return new CheckoutItemVm(
                checkoutItem.getId() ,
                checkoutItem.getProductId(),
                checkoutItem.getQuantity()
        );
    }
}
