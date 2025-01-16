package com.lexuancong.product.viewmodel.category;

import com.lexuancong.product.model.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;


public record CategoryPostVm(
       @NotBlank String name,
       @NotBlank String slug,
       String description,
       Long parentId,
       Long imageId,
       String metaKeywords,
       String metaDescription,
       Short displayIndex,
       boolean isPublic
) {
    public Category toModel(){
        return Category.builder()
                .name(name)
                .slug(slug)
                .description(description)
                .displayIndex(displayIndex)
                .metaKeywords(metaKeywords)
                .metaDescription(metaDescription)
                .imageId(imageId)
                .isPublic(isPublic)
                .build();
    }
}
