package com.lexuancong.oder.kafka.message;

import com.lexuancong.oder.model.enum_status.PaymentStatus;
import lombok.Builder;

@Builder
public record PaymentResultMessage(
        Long orderId,
        Long paymentId,
        PaymentStatus status


) {
}
