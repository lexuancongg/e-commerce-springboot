package com.lexuancong.inventory.dto.stock;

import jakarta.validation.constraints.NotNull;

public record StockCreateRequest(
        @NotNull Long productId,
        @NotNull Long warehouseId
) {
}
