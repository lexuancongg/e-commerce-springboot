package com.lexuancong.product.dto.specificproductvariant;

import com.lexuancong.product.model.SpecificProductVariant;

public record SpecificProductVariantGetResponse(
        Long id,
        Long productOptionId,
        Long productId,
        String productOptionName,
        String productOptionValue
) {
    public static SpecificProductVariantGetResponse fromSpecificProductVariant(SpecificProductVariant specificProductVariant) {
        return new SpecificProductVariantGetResponse(
                specificProductVariant.getId(),
                specificProductVariant.getProductOption().getId(),
                specificProductVariant.getProduct().getId(),
                specificProductVariant.getProductOption().getName(),
                specificProductVariant.getValue()
        );
    }
}
