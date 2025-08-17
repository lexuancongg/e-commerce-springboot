package com.lexuancong.payment.service.handler.providers;

import com.lexuancong.payment.model.InitiatedPaymentVm;
import com.lexuancong.payment.model.enumeration.PaymentMethod;
import com.lexuancong.payment.repository.PaymentProviderRepository;
import com.lexuancong.payment.viewmodel.InitPaymentRequest;
import org.springframework.stereotype.Service;

@Service
public class PaypalPaymentHandler extends AbstractPaymentProviderSupport implements ProviderPaymentHandler{
    public PaypalPaymentHandler(final PaymentProviderRepository paymentProviderRepository) {
        super(paymentProviderRepository);
    }
    @Override
    public String getNameProvider() {
        return PaymentMethod.PAYPAL.name();
    }

    @Override
    public InitiatedPaymentVm initPayment(InitPaymentRequest initPaymentRequest) {
        return null;
    }
}
