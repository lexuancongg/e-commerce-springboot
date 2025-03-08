package com.lexuancong.product.viewmodel.attributegroup;

import com.lexuancong.product.model.attribute.ProductAttributeGroup;
import jakarta.validation.constraints.NotBlank;

public record  ProductAttributeGroupPostVm(@NotBlank String name) {
    public ProductAttributeGroup toProductAttributeGroup() {
        ProductAttributeGroup productAttributeGroup = new ProductAttributeGroup();
        productAttributeGroup.setName(this.name);
        return productAttributeGroup;
    }
}
