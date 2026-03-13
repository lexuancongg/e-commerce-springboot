package com.lexuancong.inventory.dto.stock;

import com.lexuancong.inventory.model.Stock;
import com.lexuancong.inventory.dto.product.ProductInfoResponse;

public record StockDetailResponse(
        Long id,
        Long productId,
        String productName,
        int quantity,
        int lockedQuantity,
        Long warehouseId,
        String warehouseName,
        String productSku
) {
    public static StockDetailResponse fromStock(Stock stock , ProductInfoResponse productInfoResponse){
        return new StockDetailResponse(stock.getId(),
                stock.getProductId(),
                productInfoResponse.name(),
                stock.getQuantity(),
                stock.getLockedQuantity(),
                stock.getWarehouse().getId(),
                stock.getWarehouse().getName(),
                productInfoResponse.sku()

                );
    }
}
