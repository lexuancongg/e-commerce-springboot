package com.lexuancong.product.viewmodel.product;

import jakarta.validation.constraints.NotNull;

public record ProductSubtractQuantityVm(@NotNull Long productId, @NotNull Long quantity) {
}
