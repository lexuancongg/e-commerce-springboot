package com.lexuancong.product.viewmodel.product.variants;

import com.lexuancong.product.viewmodel.image.ImageVm;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record ProductVariantVm(
        Long id,
        String name,
        String slug,
        String sku,
        String gtin,
        BigDecimal price,
        String avatarUrl,
        List<ImageVm>  productImages,
        Map<Long,String> optionValues

) {
}
