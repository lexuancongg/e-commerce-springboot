package com.lexuancong.oder.viewmodel.checkout.checkoutitem;

import com.lexuancong.oder.model.Checkout;
import com.lexuancong.oder.model.CheckoutItem;
import com.lexuancong.oder.viewmodel.product.ProductCheckoutPreviewVm;
import jakarta.validation.constraints.Positive;

public record CheckoutItemPostVm(
        Long productId,
        @Positive
        int quantity
) {
        public  CheckoutItem toModel(Checkout checkout, ProductCheckoutPreviewVm productCheckoutPreviewVm) {
                return CheckoutItem.builder()
                        .productId(productId)
                        .quantity(quantity)
                        .checkout(checkout)
                        .price(productCheckoutPreviewVm.price())
                        .productName(productCheckoutPreviewVm.name())
                        .build();

        }
}
