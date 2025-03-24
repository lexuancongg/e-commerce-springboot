package com.lexuancong.product.viewmodel.product;

import java.util.List;

public record ProductDetailVm(
        Long id,
        String name,
        String brandName,
        List<String> categories,
        List<AttributeGroupVm> attributeGroups,
        String shortDescription,
        String description,
        String specifications,
        Double price,
        boolean hasOptions,
        String avatarImageUrl,
        boolean isFeatured,
        List<String> images,
        boolean isOrderEnable
) {
}
