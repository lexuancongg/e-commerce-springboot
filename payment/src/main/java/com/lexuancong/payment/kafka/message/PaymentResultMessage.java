package com.lexuancong.payment.kafka.message;

import com.lexuancong.payment.model.enumeration.PaymentStatus;
import lombok.Builder;

@Builder
public record PaymentResultMessage(
        Long orderId,
        Long paymentId,
        PaymentStatus status


) {
}
