package com.lexuancong.inventory.viewmodel.stock;

import jakarta.validation.constraints.NotNull;

public record StockPostVm(
        @NotNull Long productId
) {
}
