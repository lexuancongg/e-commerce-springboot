package com.lexuancong.oder.viewmodel;

import java.math.BigDecimal;

public record OrderItemVm(
        Long id,
        Long productId,
        int quantity,
        BigDecimal productPrice,
        BigDecimal totalPrice,
        Long orderId

) {
}
