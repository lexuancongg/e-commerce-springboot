package com.lexuancong.product.viewmodel.brand;

import com.lexuancong.product.model.Brand;
import lombok.Builder;

@Builder
public record BrandVm(Long id, String name, String slug, boolean isPublic) {
    public static BrandVm fromModel(Brand brand) {
        return BrandVm.builder()
                .id(brand.getId()).slug(brand.getSlug()).name(brand.getName()).isPublic(brand.isPublic())
                .build();
    }

}
