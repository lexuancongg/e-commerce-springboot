package com.lexuancong.paypalpayment.dto;

import lombok.Builder;

@Builder
public record PaypalCaptureResponse(
        String status,
        Long paymentId
) {
}
