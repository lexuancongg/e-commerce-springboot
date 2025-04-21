package com.lexuancong.payment.viewmodel;

import com.lexuancong.payment.model.PaymentProvider;

public record PaymentProviderVm(
        Long id,
        String name,
        String configureUrl,
        String properties
) {
    public  static PaymentProviderVm   fromModel(PaymentProvider paymentProvider){
        return new PaymentProviderVm(
                paymentProvider.getId(),paymentProvider.getName(),
                paymentProvider.getConfigureUrl(),paymentProvider.getProperties()
        );
    }
}
