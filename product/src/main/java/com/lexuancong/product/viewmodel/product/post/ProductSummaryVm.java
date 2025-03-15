package com.lexuancong.product.viewmodel.product.post;

import com.lexuancong.product.model.Product;

public record ProductSummaryVm(Long id, String name, String slug) {
    public static ProductSummaryVm fromModel(Product product) {
        return new ProductSummaryVm(product.getId(), product.getName(), product.getSlug());
    }
}
