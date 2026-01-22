package com.lexuancong.product.dto.product.variants;

import com.lexuancong.product.model.Product;
import com.lexuancong.product.dto.product.databinding.ProductVariationPropertiesRequire;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

// recod tự động sinh getter => không cần overight method
public record ProductVariationCreateRequest(
        Long id,
        String name,
        String slug,
        String sku,
        BigDecimal price,
        Long avatarImageId,
        List<Long> imageIds,
        Map<Long, String> valueOfOptionByOptionId


) implements ProductVariationPropertiesRequire {

    public Product toProduct(){
        Product product = new Product();
        product.setName(name);
        product.setSlug(slug);
        product.setSku(sku);
        product.setPrice(price);
        product.setAvatarImageId(avatarImageId);
        return product;

    }
}
