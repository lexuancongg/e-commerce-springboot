package com.lexuancong.inventory.dto.stock;

import com.lexuancong.inventory.model.Stock;
import com.lexuancong.inventory.dto.product.ProductInfoGetResponse;

public record StockGetResponse(
        Long id,
        Long productId,
        String productName,
        int quantity,
        int lockedQuantity,
        Long warehouseId,
        String warehouseName,
        String productSku
) {
    public static StockGetResponse fromStock(Stock stock , ProductInfoGetResponse productInfoGetResponse){
        return new StockGetResponse(stock.getId(),
                stock.getProductId(),
                productInfoGetResponse.name(),
                stock.getQuantity(),
                stock.getLockedQuantity(),
                stock.getWarehouse().getId(),
                stock.getWarehouse().getName(),
                productInfoGetResponse.sku()

                );
    }
}
