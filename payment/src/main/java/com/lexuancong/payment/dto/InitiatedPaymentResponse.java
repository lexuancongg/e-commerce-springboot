package com.lexuancong.payment.dto;

import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record InitiatedPaymentResponse(
        String redirectUrl,
        String transactionId,
         Long orderId,
         LocalDateTime expiredTime,
         String qrCode,
         String status,
         String provider,
         String signature,
        Long paymentId
) {
}
