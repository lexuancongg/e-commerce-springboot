package com.lexuancong.product.dto.productoptionvalue;

import com.lexuancong.product.model.ProductOptionValue;

public record ProductOptionValueResponse(
        Long productId ,
        Long id ,
        String productName,
        Long productOptionId,
        String productOptionName,
        String value
) {
    public static ProductOptionValueResponse fromProductOptionValue(ProductOptionValue productOptionValue) {
       return new ProductOptionValueResponse(
                productOptionValue.getProduct().getId(),
                productOptionValue.getId(),
                productOptionValue.getProduct().getName(),
                productOptionValue.getProductOption().getId(),
                productOptionValue.getProductOption().getName(),
                productOptionValue.getValue()

        );

    }
}
