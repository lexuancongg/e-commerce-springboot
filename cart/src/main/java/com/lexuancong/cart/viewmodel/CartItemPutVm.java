package com.lexuancong.cart.viewmodel;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemPutVm(@NotNull @Min(1) int quantity) {
}
