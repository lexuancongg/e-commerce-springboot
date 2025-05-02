package com.lexuancong.payment.model;

import java.time.LocalDateTime;

public record InitiatedPayment(
        String redirectUrl,  String transactionId,
         String orderId,
         LocalDateTime expiredTime,
         String qrCode,
         String provider,
         String signature,
        String paymentId
) {
}
