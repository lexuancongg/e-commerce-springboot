package com.lexuancong.vnpaypayment.dto;

import lombok.Builder;

import java.math.BigDecimal;
@Builder
public record VnpayCreatePaymentUrlRequest(BigDecimal totalPrice, String paymentMethod,
                                           String providerProperties,String description
) {

}
