package com.lexuancong.product.viewmodel.product.post;

import com.lexuancong.product.viewmodel.product.databinding.ProductVariationPropertiesRequire;

import java.util.List;
import java.util.Map;

// recod tự động sinh getter => không cần overight method
public record ProductVariationPostVm(
        String name,
        String slug,
        String sku,
        String gtin,
        Double price,
        Long avatarImageId,
        List<Long> productImageIds,
        Map<Long, String> valueOfOptionByOptionId


) implements ProductVariationPropertiesRequire {
    @Override
    public Long id() {
        return null;
    }
}
