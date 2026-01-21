package com.lexuancong.product.dto.brand;

import com.lexuancong.product.model.Brand;
import lombok.Builder;

@Builder
public record BrandGetResponse(Long id, String name, String slug, boolean isPublic) {
    public static BrandGetResponse fromBrand(Brand brand) {
        return BrandGetResponse.builder()
                .id(brand.getId()).slug(brand.getSlug()).name(brand.getName()).isPublic(brand.isPublic())
                .build();
    }

}
