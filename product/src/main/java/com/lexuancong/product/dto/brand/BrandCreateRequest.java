package com.lexuancong.product.dto.brand;

import com.lexuancong.product.model.Brand;
import jakarta.validation.constraints.NotBlank;

public record BrandCreateRequest(
        @NotBlank String name,
        @NotBlank String slug,
        boolean isPublic
) {
    public Brand toBrand() {
        return Brand.builder()
                .name(name).slug(slug).isPublic(isPublic)
                .build();
    }
}
