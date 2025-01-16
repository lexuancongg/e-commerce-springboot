package com.lexuancong.product.viewmodel.category;

import com.lexuancong.product.model.Category;
import com.lexuancong.product.viewmodel.image.ImageVm;
import lombok.Builder;

@Builder
public record CategoryVm(Long id, String name, String slug, ImageVm image) {
    public static CategoryVm fromModel(Category category) {
        return new CategoryVm(category.getId(), category.getName(), category.getSlug(), null);
    }
}
