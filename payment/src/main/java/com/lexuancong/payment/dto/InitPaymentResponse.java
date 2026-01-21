package com.lexuancong.payment.dto;

public record InitPaymentResponse(
        String status, String paymentId, String redirectUrl
) {
}
