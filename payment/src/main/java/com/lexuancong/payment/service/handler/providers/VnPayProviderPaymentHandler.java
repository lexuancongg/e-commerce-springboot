package com.lexuancong.payment.service.handler.providers;

import com.lexuancong.payment.model.InitiatedPayment;
import com.lexuancong.payment.model.enumeration.PaymentMethod;
import com.lexuancong.payment.viewmodel.InitPaymentRequest;

// dành cho vnpay
public class VnPayProviderPaymentHandler implements ProviderPaymentHandler {
    @Override
    public String getNameProvider() {
        return PaymentMethod.VNPAY.name();
    }

    // đoạn code xử lý dành cho vn pay
    @Override
    public InitiatedPayment initPayment(InitPaymentRequest initPaymentRequest) {
        return null;
    }
}
