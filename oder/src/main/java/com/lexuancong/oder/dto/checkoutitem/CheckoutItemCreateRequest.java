package com.lexuancong.oder.dto.checkoutitem;

import com.lexuancong.oder.model.Checkout;
import com.lexuancong.oder.model.CheckoutItem;
import com.lexuancong.oder.dto.product.ProductCheckoutPreviewGetResponse;
import jakarta.validation.constraints.Positive;

public record CheckoutItemCreateRequest(
        Long productId,
        @Positive
        int quantity
) {
        public  CheckoutItem toCheckoutItem(Checkout checkout, ProductCheckoutPreviewGetResponse productCheckoutPreviewGetResponse) {
                return CheckoutItem.builder()
                        .productId(productId)
                        .quantity(quantity)
                        .checkout(checkout)
                        .price(productCheckoutPreviewGetResponse.price())
                        .productName(productCheckoutPreviewGetResponse.name())
                        .build();

        }
}
