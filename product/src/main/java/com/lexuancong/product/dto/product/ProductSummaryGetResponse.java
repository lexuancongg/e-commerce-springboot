package com.lexuancong.product.dto.product;

import com.lexuancong.product.model.Product;

// tóm tắt cơ bản nhất cuar sp khi thêm thành công
public record ProductSummaryGetResponse(Long id, String name, String slug) {
    public static ProductSummaryGetResponse fromModel(Product product) {
        return new ProductSummaryGetResponse(product.getId(), product.getName(), product.getSlug());
    }
}
