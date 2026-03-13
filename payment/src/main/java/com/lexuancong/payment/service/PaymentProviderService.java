package com.lexuancong.payment.service;

import com.lexuancong.payment.model.PaymentProvider;
import com.lexuancong.payment.repository.PaymentProviderRepository;
import com.lexuancong.payment.dto.PaymentProviderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentProviderService {
    private final PaymentProviderRepository paymentProviderRepository;


    public List<PaymentProviderResponse> getPaymentProviders(){
        List<PaymentProvider> paymentProviders = paymentProviderRepository.findByEnabledIsTrue();
        return paymentProviders.stream()
                .map(PaymentProviderResponse::fromPaymentProvider)
                .toList();

    }
}
