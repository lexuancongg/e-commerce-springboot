package com.lexuancong.oder.dto.checkout;

import com.lexuancong.oder.model.Checkout;
import com.lexuancong.oder.dto.checkoutitem.CheckoutItemResponse;

import java.util.List;

public record CheckoutResponse(
        Long id,
        String email,
        String note,
        List<CheckoutItemResponse> checkoutItems

){
    public static CheckoutResponse fromCheckout(Checkout checkout){
        List<CheckoutItemResponse> checkoutItems = checkout.getCheckoutItems().stream()
                .map(CheckoutItemResponse::fromCheckoutItem)
                .toList();
        return new CheckoutResponse(

                checkout.getId(),
                checkout.getEmail(),
                checkout.getNote(),
                checkoutItems
        );
    }


}
