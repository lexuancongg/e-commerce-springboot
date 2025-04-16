package com.lexuancong.oder.viewmodel.checkout;

import com.lexuancong.oder.model.Checkout;
import com.lexuancong.oder.viewmodel.checkout.checkoutitem.CheckoutItemPostVm;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CheckoutPostVm (
        String email,
        @NotEmpty(message = "Checkout Items must not be empty")
        List<CheckoutItemPostVm> checkoutItemPostVms
){
    public Checkout toModel(){
        return Checkout.builder()
                .email(email)
                .build();
    }
}
