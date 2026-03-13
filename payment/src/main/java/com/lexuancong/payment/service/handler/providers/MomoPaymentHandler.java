package com.lexuancong.payment.service.handler.providers;

import com.lexuancong.payment.dto.InitiatedPaymentResponse;
import com.lexuancong.payment.model.enumeration.PaymentMethod;
import com.lexuancong.payment.dto.InitPaymentRequest;

public class MomoPaymentHandler implements ProviderPaymentHandler {
    @Override
    public String getNameProvider() {
        return PaymentMethod.MOMO.name();
    }

    @Override
    public InitiatedPaymentResponse initPayment(InitPaymentRequest initPaymentRequest) {
        return null;
    }
}
