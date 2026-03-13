package com.lexuancong.paypalpayment.dto;

import lombok.Builder;

@Builder
public record PaypalCaptureRequest(
        Long paymentId,
        Long orderId,
        String token
) {
}
