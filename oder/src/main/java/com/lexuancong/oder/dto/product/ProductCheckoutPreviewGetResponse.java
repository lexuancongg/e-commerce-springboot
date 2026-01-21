package com.lexuancong.oder.dto.product;

import java.math.BigDecimal;

public record ProductCheckoutPreviewGetResponse(
        Long id,
        String name,
        BigDecimal price
) {
}
