package com.lexuancong.payment.service.handler.providers;

import com.lexuancong.payment.model.PaymentProvider;
import com.lexuancong.payment.repository.PaymentProviderRepository;

// lấy thông tin cấu hình cố định trong db của provider
public abstract class AbstractPaymentProviderSupport {
    private final PaymentProviderRepository paymentProviderRepository;
    public AbstractPaymentProviderSupport(final PaymentProviderRepository paymentProviderRepository) {
        this.paymentProviderRepository = paymentProviderRepository;
    }
    public String getConfigurationProperties(String paymentProviderId){
        PaymentProvider paymentProvider = this.paymentProviderRepository.findById(paymentProviderId)
                .orElseThrow(() ->  new RuntimeException("provider not found"));
        return paymentProvider.getProperties();

    }

}
