package com.lexuancong.product.dto.category;

import com.lexuancong.product.model.Category;
import lombok.Builder;

@Builder
public record CategoryGetResponse(
        Long id,
        String name,
        String slug,
        String avatarUrl) {

    public static CategoryGetResponse fromCategory(Category category) {
        return new CategoryGetResponse(category.getId(), category.getName(), category.getSlug(), null);
    }
}
