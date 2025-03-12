package com.lexuancong.product.viewmodel.productattributevalue;

import com.lexuancong.product.model.ProductAttributeValue;

public record ProductAttributeValueVm(Long id, String ProductAttributeName, String value) {
    public static ProductAttributeValueVm fromModel(ProductAttributeValue productAttributeValue) {
        return new ProductAttributeValueVm(productAttributeValue.getId(),
                productAttributeValue.getProductAttribute().getName(),
                productAttributeValue.getValue());

    }

}
