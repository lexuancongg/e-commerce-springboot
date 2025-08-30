package com.lexuancong.product.viewmodel.productoptionvalue;

import com.lexuancong.product.model.ProductOptionValue;
import com.lexuancong.product.viewmodel.productoptions.ProductOptionGetVm;

public record ProductOptionValueGetVm(Long productId , Long id , String productName,
                                      Long productOptionId, String productOptionName, String value) {
    public static ProductOptionValueGetVm fromModel(ProductOptionValue productOptionValue) {
       return new ProductOptionValueGetVm(
                productOptionValue.getProduct().getId(),
                productOptionValue.getId(),
                productOptionValue.getProduct().getName(),
                productOptionValue.getProductOption().getId(),
                productOptionValue.getProductOption().getName(),
                productOptionValue.getValue()

        );

    }
}
