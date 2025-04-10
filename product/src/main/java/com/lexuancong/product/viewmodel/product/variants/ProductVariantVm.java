package com.lexuancong.product.viewmodel.product.variants;

import com.lexuancong.product.viewmodel.image.ImageVm;

import java.util.List;
import java.util.Map;

public record ProductVariantVm(
        Long id,
        String name,
        String slug,
        String sku,
        String gtin,
        Double price,
        String avatarUrl,
        List<ImageVm>  productImages,
        Map<Long,String> optionValues

) {
}
