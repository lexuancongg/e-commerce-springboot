package com.lexuancong.product.viewmodel.brand;

import com.lexuancong.product.model.Brand;
import jakarta.validation.constraints.NotBlank;

public record BrandPostVm(
        @NotBlank String name,
        @NotBlank String slug,
        boolean isPublic
) {
    public Brand toModel() {
        return Brand.builder()
                .name(name).slug(slug).isPublic(isPublic)
                .build();
    }
}
