package com.lexuancong.oder.dto.checkoutitem;

import com.lexuancong.oder.model.Checkout;
import com.lexuancong.oder.model.CheckoutItem;
import com.lexuancong.oder.dto.product.ProductInfoResponse;
import jakarta.validation.constraints.Positive;

public record CheckoutItemCreateRequest(
        Long productId,
        @Positive
        int quantity
) {
        public  CheckoutItem toCheckoutItem(Checkout checkout, ProductInfoResponse productInfoResponse) {
                return CheckoutItem.builder()
                        .productId(productId)
                        .quantity(quantity)
                        .checkout(checkout)
                        .price(productInfoResponse.price())
                        .productName(productInfoResponse.name())
                        .build();

        }
}
