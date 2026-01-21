package com.lexuancong.product.dto.productoptions;

import com.lexuancong.product.model.ProductOption;

public record ProductOptionGetResponse(Long id, String name) {
    public static ProductOptionGetResponse fromProductOption(ProductOption productOption) {
        return new ProductOptionGetResponse(productOption.getId(),productOption.getName());
    }
}
