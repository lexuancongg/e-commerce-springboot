package com.lexuancong.payment.viewmodel;

import java.math.BigDecimal;
import java.util.Map;

public record InitPaymentRequest(String paymentMethod, BigDecimal totalPrice ,
                                 String checkoutId,String orderId,
                                 String description,
                                 Map<String, String> extraData
) {
}
