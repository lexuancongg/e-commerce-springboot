package com.lexuancong.product.dto.attributegroup;

import com.lexuancong.product.model.attribute.ProductAttributeGroup;
import jakarta.validation.constraints.NotBlank;

public record ProductAttributeGroupCreateRequest(@NotBlank String name) {
    public ProductAttributeGroup toProductAttributeGroup() {
        ProductAttributeGroup productAttributeGroup = new ProductAttributeGroup();
        productAttributeGroup.setName(this.name);
        return productAttributeGroup;
    }
}
