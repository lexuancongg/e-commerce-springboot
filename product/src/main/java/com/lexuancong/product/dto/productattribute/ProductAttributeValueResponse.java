package com.lexuancong.product.dto.productattribute;

import com.lexuancong.product.model.ProductAttributeValue;

public record ProductAttributeValueResponse(Long id, String attributeName, String value) {
    public static ProductAttributeValueResponse fromProductAttributeValue(ProductAttributeValue productAttributeValue) {
        return new ProductAttributeValueResponse(productAttributeValue.getId(),
                productAttributeValue.getProductAttribute().getName(),
                productAttributeValue.getValue());

    }

}
