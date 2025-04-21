package com.lexuancong.payment.service;

import com.lexuancong.payment.model.PaymentProvider;
import com.lexuancong.payment.repository.PaymentProviderRepository;
import com.lexuancong.payment.viewmodel.PaymentProviderVm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentProviderService {
    private final PaymentProviderRepository paymentProviderRepository;


    public List<PaymentProviderVm> getPaymentProviders(){
        List<PaymentProvider> paymentProviders = paymentProviderRepository.findByEnabledIsTrue();
        return paymentProviders.stream()
                .map(PaymentProviderVm::fromModel)
                .toList();

    }
}
