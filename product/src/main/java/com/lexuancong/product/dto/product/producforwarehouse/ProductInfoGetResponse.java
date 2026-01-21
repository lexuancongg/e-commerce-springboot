package com.lexuancong.product.dto.product.producforwarehouse;

import com.lexuancong.product.model.Product;

public record ProductInfoGetResponse(
        Long id,
        String name, String sku
) {
    public static ProductInfoGetResponse fromProduct(Product product) {
        return new ProductInfoGetResponse(product.getId(), product.getName(), product.getSku());
    }
}
