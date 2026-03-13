package com.lexuancong.payment.dto;

import com.lexuancong.payment.model.enumeration.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CapturePaymentRequest(
       @NotBlank String paymentMethod,
        @NotNull Long orderId,
       @NotBlank Long paymentId,
        String token
) {
}
