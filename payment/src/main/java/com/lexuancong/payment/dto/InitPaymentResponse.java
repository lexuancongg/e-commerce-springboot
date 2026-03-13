package com.lexuancong.payment.dto;

public record InitPaymentResponse(
        String status,
        Long paymentId,
        String redirectUrl
) {
}
