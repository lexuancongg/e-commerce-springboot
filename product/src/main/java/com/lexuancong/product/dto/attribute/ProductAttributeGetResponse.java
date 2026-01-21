package com.lexuancong.product.dto.attribute;

import com.lexuancong.product.model.attribute.ProductAttribute;
import lombok.Builder;

@Builder
public record ProductAttributeGetResponse(Long id, String name, String productAttributeGroup) {
    public static ProductAttributeGetResponse fromProductAttribute(ProductAttribute productAttribute) {
        String productAttributeGroup = productAttribute.getProductAttributeGroup()==null
                ? null : productAttribute.getProductAttributeGroup().getName();
        return new ProductAttributeGetResponse(productAttribute.getId(), productAttribute.getName(), productAttributeGroup);
    }

}
