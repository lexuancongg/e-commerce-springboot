package com.lexuancong.product.dto.product;

import com.lexuancong.product.model.Product;

public record ProductSummaryResponse(Long id, String name, String slug) {
    public static ProductSummaryResponse fromProduct(Product product) {
        return new ProductSummaryResponse(product.getId(), product.getName(), product.getSlug());
    }
}
