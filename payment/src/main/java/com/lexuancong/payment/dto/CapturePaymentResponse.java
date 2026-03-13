package com.lexuancong.payment.dto;

import com.lexuancong.payment.model.enumeration.PaymentStatus;
import lombok.Builder;

@Builder
public record CapturePaymentResponse(
        PaymentStatus status,
        Long paymentId
) {
}
