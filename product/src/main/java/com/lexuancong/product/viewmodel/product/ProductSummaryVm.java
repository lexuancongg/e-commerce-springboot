package com.lexuancong.product.viewmodel.product;

import com.lexuancong.product.model.Product;

// tóm tắt cơ bản nhất cuar sp khi thêm thành công
public record ProductSummaryVm(Long id, String name, String slug) {
    public static ProductSummaryVm fromModel(Product product) {
        return new ProductSummaryVm(product.getId(), product.getName(), product.getSlug());
    }
}
