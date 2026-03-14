package com.lexuancong.product.dto.brand;

import com.lexuancong.product.model.Brand;
import lombok.Builder;

@Builder
public record BrandResponse(Long id, String name, String slug, boolean isPublic) {
    public static BrandResponse fromBrand(Brand brand) {
        return BrandResponse.builder()
                .id(brand.getId())
                .slug(brand.getSlug())
                .name(brand.getName())
                .isPublic(brand.isPublic())
                .build();
    }

}
