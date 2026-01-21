package com.lexuancong.oder.dto.checkout;

import com.lexuancong.oder.model.Checkout;
import com.lexuancong.oder.dto.checkoutitem.CheckoutItemGetResponse;

import java.util.List;

public record CheckoutGetResponse(
        Long id,
        String email,
        String note,
        List<CheckoutItemGetResponse> checkoutItemGetResponses

){
    public static CheckoutGetResponse fromModel(Checkout checkout){
        List<CheckoutItemGetResponse> checkoutItems = checkout.getCheckoutItems().stream()
                .map(CheckoutItemGetResponse::fromCheckoutItem)
                .toList();
        return new CheckoutGetResponse(

                checkout.getId(),
                checkout.getEmail(),
                checkout.getNote(),
                checkoutItems
        );
    }


}
