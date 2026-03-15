package com.lexuancong.product.dto.productoptions;

import com.lexuancong.product.model.ProductOption;

public record ProductOptionResponse(Long id, String name) {
    public static ProductOptionResponse fromProductOption(ProductOption productOption) {
        return new ProductOptionResponse(productOption.getId(),productOption.getName());
    }
}
