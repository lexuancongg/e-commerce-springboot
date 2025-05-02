package com.lexuancong.payment.viewmodel;

import java.awt.*;

public record InitPaymentResponse(
        String status, String paymentId, String redirectUrl
) {
}
