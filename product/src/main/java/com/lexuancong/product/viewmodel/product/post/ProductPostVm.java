package com.lexuancong.product.viewmodel.product.post;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lexuancong.product.validation.ValidateProductPrice;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
// dùng để tùy chỉnh quá trình Serialization
@JsonSerialize
public record ProductPostVm(
        @NotBlank String name,
        @NotBlank String slug,
        Long brandId,
        List<Long> categoryIds,
        String shortDescription,
        String description,
        String specification,
        String sku,
        String gtin,
        @ValidateProductPrice Double price,
        boolean isPublic,
        boolean isFeature
) {
}
