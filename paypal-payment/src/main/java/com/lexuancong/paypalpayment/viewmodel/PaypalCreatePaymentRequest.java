package com.lexuancong.paypalpayment.viewmodel;

import java.math.BigDecimal;

public record PaypalCreatePaymentRequest(BigDecimal totalPrice,String orderId,String providerPaymentSetting) {
}
