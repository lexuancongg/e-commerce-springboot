package com.lexuancong.product.viewmodel.attribute;

import com.lexuancong.product.model.attribute.ProductAttribute;
import lombok.Builder;

@Builder
public record ProductAttributeVm(Long id, String name, String productAttributeGroup) {
    public static ProductAttributeVm fromModel(ProductAttribute productAttribute) {
        String productAttributeGroup = productAttribute.getProductAttributeGroup()==null
                ? null : productAttribute.getProductAttributeGroup().getName();
        return new ProductAttributeVm(productAttribute.getId(), productAttribute.getName(), productAttributeGroup);
    }

}
