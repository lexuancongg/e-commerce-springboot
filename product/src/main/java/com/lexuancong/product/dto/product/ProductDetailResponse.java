package com.lexuancong.product.dto.product;

import com.lexuancong.product.dto.productattribute.AttributeGroupValueResponse;

import java.math.BigDecimal;
import java.util.List;

public record ProductDetailResponse(
        Long id,
        String name,
        String brandName,
        List<String> categories,
        List<AttributeGroupValueResponse> attributeGroupValues,
        String shortDescription,
        String description,
        BigDecimal price,
        boolean hasOptions,
        String avatarUrl,
        boolean isFeatured,
        List<String> productImageUrls,
        boolean isOrderEnable,
        boolean isPublic
) {

}
