package com.lexuancong.product.viewmodel.product.producforwarehouse;

import com.lexuancong.product.model.Product;

public record ProductInfoVm(
        Long id,
        String name, String sku
) {
    public static ProductInfoVm fromModel(Product product) {
        return new ProductInfoVm(product.getId(), product.getName(), product.getSku());
    }
}
