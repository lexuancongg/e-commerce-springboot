package com.lexuancong.product.dto.attribute;

import com.lexuancong.product.model.attribute.ProductAttribute;
import lombok.Builder;

@Builder
public record ProductAttributeResponse(
        Long id, String
        name,
        String group
) {
    public static ProductAttributeResponse fromProductAttribute(ProductAttribute productAttribute) {
        String group = productAttribute.getGroup() ==null
                ? null : productAttribute.getGroup().getName();
        return new ProductAttributeResponse(
                productAttribute.getId(),
                productAttribute.getName(),
                group
        );
    }

}
