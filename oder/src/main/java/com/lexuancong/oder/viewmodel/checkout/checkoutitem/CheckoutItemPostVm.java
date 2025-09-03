package com.lexuancong.oder.viewmodel.checkout.checkoutitem;

import com.lexuancong.oder.model.Checkout;
import com.lexuancong.oder.model.CheckoutItem;
import jakarta.validation.constraints.Positive;

public record CheckoutItemPostVm(
        Long productId,
        @Positive
        int quantity
) {
        public  CheckoutItem toModel( Checkout checkout) {
                return CheckoutItem.builder()
                        .productId(productId)
                        .quantity(quantity)
                        .checkout(checkout)
                        .build();

        }
}
