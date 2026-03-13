package com.lexuancong.oder.dto.product;

import java.math.BigDecimal;

public record ProductInfoResponse(
        Long id,
        String name,
        BigDecimal price
) {
}
