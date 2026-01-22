package com.lexuancong.product.dto.product.variants;

import com.lexuancong.product.dto.image.ImagePreviewGetResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record ProductVariantGetResponse(
        Long id,
        String name,
        String slug,
        String sku,
        BigDecimal price,
        String avatarUrl,
        List<ImagePreviewGetResponse>  productImages,
        Map<Long,String> optionValues

) {
}
