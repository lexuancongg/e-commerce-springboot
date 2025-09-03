package com.lexuancong.oder.viewmodel.product;

import java.math.BigDecimal;

public record ProductCheckoutPreviewVm(
        Long id,
        String name,
        BigDecimal price
) {
}
