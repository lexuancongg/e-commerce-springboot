package com.lexuancong.paypalpayment.dto;

public record PaypalCreatePaymentResponse(String  status,String paymentId,String redirectUrl) {
}
