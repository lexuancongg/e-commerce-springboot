package com.lexuancong.oder.dto.checkout;

import com.lexuancong.oder.model.Checkout;
import com.lexuancong.oder.model.enum_status.CheckoutStatus;
import com.lexuancong.oder.dto.checkoutitem.CheckoutItemCreateRequest;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;
import java.util.List;

public record CheckoutCreateRequest(
        String email,
        @NotEmpty(message = "Checkout Items must not be empty")
        List<CheckoutItemCreateRequest> checkoutItemCreateRequests,
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
