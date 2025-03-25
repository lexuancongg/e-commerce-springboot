package com.lexuancong.oder.viewmodel;

import java.math.BigDecimal;

public record OrderItemPostVm(
        Long productId,
        int quantity,
        BigDecimal productPrice,
        BigDecimal totalPrice,
        String note
) {
}
