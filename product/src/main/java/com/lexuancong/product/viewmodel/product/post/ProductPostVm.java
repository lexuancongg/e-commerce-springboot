package com.lexuancong.product.viewmodel.product.post;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lexuancong.product.model.Product;
import com.lexuancong.product.model.ProductOptionValue;
import com.lexuancong.product.validation.ValidateProductPrice;
import com.lexuancong.product.viewmodel.product.ProductOptionPostValueVm;
import com.lexuancong.product.viewmodel.product.databinding.ProductPropertiesRequire;
import jakarta.validation.constraints.NotBlank;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
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
        List<ProductVariationPostVm> variations,
        List<ProductOptionPostValueVm> productOptionValues
)  implements ProductPropertiesRequire<ProductVariationPostVm> {


    public Product toModel(){
        Product product = new Product();
        product.setName(name);
        product.setSlug(slug);
        product.setAvatarImageId(avatarImageId);
        product.setDescription(description);
        product.setSku(slug);
        product.setShortDescription(shortDescription);
        product.setSpecifications(specification);
        product.setGtin(gtin);
        product.setPrice(price);
        product.setPublic(isPublic);
        product.setFeature(isFeature);
        product.setWeight(weight);
        product.setHeight(height);
        product.setWidth(width);
        product.setHasOptions(!CollectionUtils.isEmpty(variations) && !this.productOptionValues.isEmpty());
    }

}
