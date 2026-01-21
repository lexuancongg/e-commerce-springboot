package com.lexuancong.product.dto.product;

import com.lexuancong.product.dto.productattribute.AttributeGroupValueGetResponse;

import java.math.BigDecimal;
import java.util.List;

public record ProductDetailGetResponse(
        Long id,
        String name,
        String brandName,
        List<String> categories,
        List<AttributeGroupValueGetResponse> attributeGroupValues,
        String shortDescription,
        String description,
        String specifications,
        BigDecimal price,
        boolean hasOptions,
        String avatarUrl,
        boolean isFeatured,
        List<String> productImageUrls,
        boolean isOrderEnable,
        boolean isPublic
) {

}
