package com.lexuancong.product.dto.attributegroup;

import com.lexuancong.product.model.attribute.ProductAttributeGroup;
import lombok.Builder;

@Builder
public record ProductAttributeGroupResponse(Long id, String name) {
    public static ProductAttributeGroupResponse fromProductAttributeGroup(ProductAttributeGroup productAttributeGroup) {
        return new ProductAttributeGroupResponse(productAttributeGroup.getId(), productAttributeGroup.getName());
    }
}
