package com.lexuancong.oder.viewmodel;

import com.lexuancong.oder.model.Order;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record OrderPostVm(
        @NotBlank String email,
        @NotNull ShippingAddressVm shippingAddressVm,
        String note,
        int numberItem,
        @NotNull BigDecimal totalPrice,
        @NotNull
        List<OrderItemPostVm> orderItemPostVms

        ) {
    public Order toOrder() {

    }

}
