package com.lexuancong.product.viewmodel.product.post;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lexuancong.product.validation.ValidateProductPrice;
import com.lexuancong.product.viewmodel.product.databinding.ProductPropertiesRequire;
import com.lexuancong.product.viewmodel.product.databinding.ProductVariationPropertiesRequire;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;

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
        boolean isFeature,
        List<Long> productImageIds,
        Long avatarImageId,
        Double length,
        Double width,
        Double height,
        Double weight,
        List<ProductVariationPostVm> variations
)  implements ProductPropertiesRequire<ProductVariationPostVm> {
}
