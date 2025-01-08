package com.lexuancong.cart.viewmodel;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemPostVm(@NotNull Long productId, @NotNull @Min(1) int quantity) {
}
