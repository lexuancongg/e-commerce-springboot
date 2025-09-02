package com.lexuancong.inventory.viewmodel.stock;

public record StockGetVm(
        Long id,
        Long productId,
        String productName,
        int quantity,
        int lockedQuantity,
        Long warehouseId,
        String warehouseName,
        String productSku
) {
}
