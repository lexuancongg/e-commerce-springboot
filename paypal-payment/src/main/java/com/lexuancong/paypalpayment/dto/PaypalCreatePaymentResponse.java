package com.lexuancong.paypalpayment.dto;

public record PaypalCreatePaymentResponse(String  status,
                                          Long paymentId,
                                          String redirectUrl) {
}
