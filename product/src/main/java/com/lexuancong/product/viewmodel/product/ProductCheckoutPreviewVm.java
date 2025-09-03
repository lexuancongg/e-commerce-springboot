package com.lexuancong.product.viewmodel.product;

import com.lexuancong.product.model.Product;

import java.math.BigDecimal;

public record ProductCheckoutPreviewVm(
        Long id,
        String name,
        BigDecimal price
) {
    public static ProductCheckoutPreviewVm fromModel(Product product) {
        return new ProductCheckoutPreviewVm(product.getId(), product.getName(), product.getPrice());
    }
}
