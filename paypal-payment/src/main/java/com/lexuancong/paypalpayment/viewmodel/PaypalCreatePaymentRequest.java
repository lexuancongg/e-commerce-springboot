package com.lexuancong.paypalpayment.viewmodel;

import lombok.Builder;

import java.math.BigDecimal;
@Builder
public record PaypalCreatePaymentRequest(BigDecimal totalPrice,String orderId,String providerPaymentSetting) {
}
