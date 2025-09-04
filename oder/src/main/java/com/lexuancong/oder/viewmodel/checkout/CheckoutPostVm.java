package com.lexuancong.oder.viewmodel.checkout;

import com.lexuancong.oder.model.Checkout;
import com.lexuancong.oder.model.enum_status.CheckoutStatus;
import com.lexuancong.oder.viewmodel.checkoutitem.CheckoutItemPostVm;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;
import java.util.List;

public record CheckoutPostVm (
        String email,
        @NotEmpty(message = "Checkout Items must not be empty")
        List<CheckoutItemPostVm> checkoutItemPostVms,
        BigDecimal totalPrice
){
    public Checkout toModel(){
        return Checkout.builder()
                .totalPrice(totalPrice)
                .email(email)
                .checkoutStatus(CheckoutStatus.PENDING)
                .build();
    }
}
