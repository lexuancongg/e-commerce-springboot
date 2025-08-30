package com.lexuancong.product.viewmodel.specificproductvariant;

import com.lexuancong.product.model.SpecificProductVariant;

public record SpecificProductVariantGetVm(
        Long id,
        Long productOptionId,
        Long productId,
        String productOptionName,
        String productOptionValue
) {
    public static SpecificProductVariantGetVm fromModel(SpecificProductVariant specificProductVariant) {
        return new SpecificProductVariantGetVm(
                specificProductVariant.getId(),
                specificProductVariant.getProductOption().getId(),
                specificProductVariant.getProduct().getId(),
                specificProductVariant.getProductOption().getName(),
                specificProductVariant.getValue()
        );
    }
}
