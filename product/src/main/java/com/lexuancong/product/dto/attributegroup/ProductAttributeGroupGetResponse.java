package com.lexuancong.product.dto.attributegroup;

import com.lexuancong.product.model.attribute.ProductAttributeGroup;

// recode không hổ trojw builder
public record ProductAttributeGroupGetResponse(Long id, String name) {
    public static ProductAttributeGroupGetResponse fromProductAttributeGroup(ProductAttributeGroup productAttributeGroup) {
        return new ProductAttributeGroupGetResponse(productAttributeGroup.getId(), productAttributeGroup.getName());
    }
}
