package com.lexuancong.product.dto.category;

import com.lexuancong.product.model.Category;
import lombok.Builder;

@Builder
public record CategoryResponse(
        Long id,
        String name,
        String slug,
        String avatarUrl
) {

    public static CategoryResponse fromCategory(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getSlug(),
                null);
    }
}
