package com.lexuancong.cart.dto.cartitem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemDeletRequest(@NotNull Long productId, @NotNull @Min(1) Integer quantity) {
}
