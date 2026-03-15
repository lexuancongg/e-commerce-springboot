package com.lexuancong.product.dto.product;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lexuancong.product.model.Product;
import com.lexuancong.product.dto.product.databinding.ProductParentPropertiesRequire;
import com.lexuancong.product.dto.productoptionvalue.ProductOptionValueCreateRequest;
import com.lexuancong.product.dto.product.variants.ProductVariationCreateRequest;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import org.springframework.util.CollectionUtils;


import java.math.BigDecimal;
import java.util.List;
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
// dùng để tùy chỉnh quá trình Serialization
@JsonSerialize
public record ProductParentCreateRequest(
        @NotBlank String name,
        @NotBlank String slug,
        Long brandId,
        List<Long> categoryIds,
        String shortDescription,
        String description,
        String sku,
        @DecimalMin(value = "0.0", inclusive = false, message = "Price phải lớn hơn 0")
        BigDecimal price,
        boolean isPublic,
        boolean isFeature,
        boolean isOrderEnable,
        Long avatarImageId,
        List<Long> imageIds,
        Double length,
        Double width,
        Double height,
        boolean isInventoryTracked,
        Double weight,
        Boolean isShownSeparately,
        List<ProductVariationCreateRequest> variations,
        List<ProductOptionValueCreateRequest> productOptionValues
)  implements ProductParentPropertiesRequire<ProductVariationCreateRequest> {




    public Product toModel(){
        Product product = new Product();
        product.setName(name);
        product.setSlug(slug);
        product.setAvatarImageId(avatarImageId);
        product.setDescription(description);
        product.setSku(slug);
        product.setShortDescription(shortDescription);
        product.setPrice(price);
        product.setPublic(isPublic);
        product.setFeature(isFeature);
        product.setWeight(weight);
        product.setHeight(height);
        product.setWidth(width);
        product.setInventoryTracked(isInventoryTracked);
        product.setShownSeparately(isShownSeparately);
        product.setHasOptions(!CollectionUtils.isEmpty(variations) && !this.productOptionValues.isEmpty());

        return product;
    }

}
