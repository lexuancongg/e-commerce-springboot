package com.lexuancong.product.dto.productattribute;

import com.lexuancong.product.model.ProductAttributeValue;

public record ProductAttributeValueGetResponse(Long id, String productAttributeName, String value) {
    public static ProductAttributeValueGetResponse fromProductAttributeValue(ProductAttributeValue productAttributeValue) {
        return new ProductAttributeValueGetResponse(productAttributeValue.getId(),
                productAttributeValue.getProductAttribute().getName(),
                productAttributeValue.getValue());

    }

}
