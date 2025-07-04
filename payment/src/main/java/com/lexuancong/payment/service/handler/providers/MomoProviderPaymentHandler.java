package com.lexuancong.payment.service.handler.providers;

import com.lexuancong.payment.model.InitiatedPayment;
import com.lexuancong.payment.viewmodel.InitPaymentRequest;

public class MomoProviderPaymentHandler implements ProviderPaymentHandler {
    @Override
    public String getNameProvider() {
        return "";
    }

    @Override
    public InitiatedPayment initPayment(InitPaymentRequest initPaymentRequest) {
        return null;
    }
}
