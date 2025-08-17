package com.lexuancong.payment.model;

import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record InitiatedPaymentVm(
        String redirectUrl,  String transactionId,
         String orderId,
         LocalDateTime expiredTime,
         String qrCode,
         String status,
         String provider,
         String signature,
        String paymentId
) {
}
