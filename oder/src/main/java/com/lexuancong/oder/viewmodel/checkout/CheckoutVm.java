package com.lexuancong.oder.viewmodel.checkout;

import com.lexuancong.oder.model.Checkout;
import com.lexuancong.oder.viewmodel.checkoutitem.CheckoutItemVm;

import java.util.List;

public record CheckoutVm (
        Long id,
        String email,
        List<CheckoutItemVm> checkoutItemVms

){
    public static CheckoutVm fromModel(Checkout checkout){
        List<CheckoutItemVm> checkoutItems = checkout.getCheckoutItems().stream()
                .map(CheckoutItemVm::fromModel)
                .toList();
        return new CheckoutVm(
                checkout.getId(),
                checkout.getEmail(),
                checkoutItems
        );
    }


}
