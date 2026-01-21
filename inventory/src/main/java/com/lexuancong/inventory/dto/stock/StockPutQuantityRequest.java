package com.lexuancong.inventory.dto.stock;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record StockPutQuantityRequest(@NotNull Long productId, @PositiveOrZero int quantity , Long stockId) {
}
