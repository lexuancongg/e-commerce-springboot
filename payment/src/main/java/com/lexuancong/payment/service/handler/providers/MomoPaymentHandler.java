package com.lexuancong.payment.service.handler.providers;

import com.lexuancong.payment.model.InitiatedPaymentVm;
import com.lexuancong.payment.viewmodel.InitPaymentRequest;

public class MomoPaymentHandler implements ProviderPaymentHandler {
    @Override
    public String getNameProvider() {
        return "";
    }

    @Override
    public InitiatedPaymentVm initPayment(InitPaymentRequest initPaymentRequest) {
        return null;
    }
}
