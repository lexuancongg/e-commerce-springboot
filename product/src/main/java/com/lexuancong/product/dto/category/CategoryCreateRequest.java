package com.lexuancong.product.dto.category;

import com.lexuancong.product.model.Category;
import jakarta.validation.constraints.NotBlank;


public record CategoryCreateRequest(
       @NotBlank String name,
       @NotBlank String slug,
       String description,
       Long parentId,
       Long imageId,
       Short displayIndex,
       boolean isPublic
) {
    public Category toCategory(){
        return Category.builder()
                .name(name)
                .slug(slug)
                .description(description)
                .displayIndex(displayIndex)
                .imageId(imageId)
                .isPublic(isPublic)
                .build();
    }
}
