package com.lexuancong.payment.service.handler.providers;

import com.lexuancong.payment.model.PaymentProvider;
import com.lexuancong.payment.repository.PaymentProviderRepository;

public abstract class AbstractPaymentProviderSupport {
    private final PaymentProviderRepository paymentProviderRepository;
    public AbstractPaymentProviderSupport(final PaymentProviderRepository paymentProviderRepository) {
        this.paymentProviderRepository = paymentProviderRepository;
    }
    public String getConfigurationProperties(String providerId){
        PaymentProvider paymentProvider = this.paymentProviderRepository.findById(providerId)
                .orElseThrow(() ->  new RuntimeException("provider not found"));
        return paymentProvider.getProperties();

    }

}
