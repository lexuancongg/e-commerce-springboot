package com.lexuancong.product.dto.specificproductvariant;

import com.lexuancong.product.model.SpecificProductVariant;

public record SpecificProductVariantResponse(
        Long id,
        Long productOptionId,
        Long productId,
        String productOptionName,
        String productOptionValue
) {
    public static SpecificProductVariantResponse fromSpecificProductVariant(SpecificProductVariant specificProductVariant) {
        return new SpecificProductVariantResponse(
                specificProductVariant.getId(),
                specificProductVariant.getProductOption().getId(),
                specificProductVariant.getProduct().getId(),
                specificProductVariant.getProductOption().getName(),
                specificProductVariant.getValue()
        );
    }
}
