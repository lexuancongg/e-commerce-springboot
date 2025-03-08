package com.lexuancong.product.viewmodel.attributegroup;

import com.lexuancong.product.model.attribute.ProductAttributeGroup;
import lombok.Builder;
// recode không hổ trojw builder
public record ProductAttributeGroupVm(Long id, String name) {
    public static ProductAttributeGroupVm fromModel(ProductAttributeGroup productAttributeGroup) {
        return new ProductAttributeGroupVm(productAttributeGroup.getId(), productAttributeGroup.getName());
    }
}
