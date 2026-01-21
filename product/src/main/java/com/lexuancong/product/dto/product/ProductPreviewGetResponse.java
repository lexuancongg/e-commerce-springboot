package com.lexuancong.product.dto.product;

import java.math.BigDecimal;

public record ProductPreviewGetResponse(
        Long id,
        String name,
        String slug,
        BigDecimal price ,
        String avatarUrl) {


}
