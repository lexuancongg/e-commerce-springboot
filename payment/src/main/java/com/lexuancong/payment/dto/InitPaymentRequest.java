package com.lexuancong.payment.dto;

import java.math.BigDecimal;
import java.util.Map;

public record InitPaymentRequest(
        String paymentMethod,
        BigDecimal totalPrice,
        Long orderId,
        Map<String, String> extraData
) {
}
