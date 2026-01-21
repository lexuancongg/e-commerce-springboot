package com.lexuancong.product.dto.productoptionvalue;

import com.lexuancong.product.model.ProductOptionValue;

public record ProductOptionValueGetResponse(
        Long productId ,
        Long id ,
        String productName,

        Long productOptionId,
        String productOptionName,
        String value
) {
    public static ProductOptionValueGetResponse fromProductOptionValue(ProductOptionValue productOptionValue) {
       return new ProductOptionValueGetResponse(
                productOptionValue.getProduct().getId(),
                productOptionValue.getId(),
                productOptionValue.getProduct().getName(),
                productOptionValue.getProductOption().getId(),
                productOptionValue.getProductOption().getName(),
                productOptionValue.getValue()

        );

    }
}
