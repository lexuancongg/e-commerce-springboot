package com.lexuancong.oder.viewmodel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderPostVm(
        @NotBlank String email,
        @NotNull ShippingAddressVm shippingAddressVm,
        String note,
        int numberItem,
        @NotNull BigDecimal totalPrice,

        ) {

}
