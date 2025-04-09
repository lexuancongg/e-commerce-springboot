package com.lexuancong.product.viewmodel.product;

import com.lexuancong.product.viewmodel.image.ImageVm;
import com.lexuancong.product.viewmodel.productattribute.AttributeGroupValueVm;

import java.util.List;

public record ProductDetailVm(
        Long id,
        String name,
        String brandName,
        List<String> categories,
        List<AttributeGroupValueVm> attributeGroupValues,
        String shortDescription,
        String description,
        String specifications,
        Double price,
        boolean hasOptions,
        String avatarUrl,
        boolean isFeatured,
        List<String> productImageUrls,
        boolean isOrderEnable,
        boolean isPublic
) {

}
