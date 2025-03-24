package com.lexuancong.product.viewmodel.product;

import com.lexuancong.product.model.Product;
import com.lexuancong.product.viewmodel.product.databinding.ProductVariationPropertiesRequire;

import java.util.List;
import java.util.Map;

// recod tự động sinh getter => không cần overight method
public record ProductVariationPostVm(
        Long id,
        String name,
        String slug,
        String sku,
        String gtin,
        Double price,
        Long avatarImageId,
        List<Long> imageIds,
        Map<Long, String> valueOfOptionByOptionId


) implements ProductVariationPropertiesRequire {

    public Product toProduct(){
        Product product = new Product();
        product.setName(name);
        product.setSlug(slug);
        product.setSku(sku);
        product.setGtin(gtin);
        product.setPrice(price);
        product.setAvatarImageId(avatarImageId);
        return product;

    }
}
