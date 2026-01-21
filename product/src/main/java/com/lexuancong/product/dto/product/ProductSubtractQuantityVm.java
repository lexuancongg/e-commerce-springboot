package com.lexuancong.product.dto.product;

import jakarta.validation.constraints.NotNull;

public record ProductSubtractQuantityVm(@NotNull Long productId, @NotNull Long quantity) {
}
