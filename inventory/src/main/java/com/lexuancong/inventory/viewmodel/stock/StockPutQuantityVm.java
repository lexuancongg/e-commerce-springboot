package com.lexuancong.inventory.viewmodel.stock;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record StockPutQuantityVm(@NotNull Long productId, @PositiveOrZero int quantity , Long stockId) {
}
