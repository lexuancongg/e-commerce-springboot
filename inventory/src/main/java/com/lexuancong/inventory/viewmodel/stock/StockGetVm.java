package com.lexuancong.inventory.viewmodel.stock;

import com.lexuancong.inventory.model.Stock;
import com.lexuancong.inventory.viewmodel.product.ProductInfoVm;

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
    public static StockGetVm fromModel(Stock stock , ProductInfoVm productInfoVm){
        return new StockGetVm(stock.getId(),
                stock.getProductId(),
                productInfoVm.name(),
                stock.getQuantity(),
                stock.getLockedQuantity(),
                stock.getWarehouse().getId(),
                stock.getWarehouse().getName(),
                productInfoVm.sku()

                );
    }
}
