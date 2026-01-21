package com.lexuancong.payment.dto;

import com.lexuancong.payment.model.PaymentProvider;

public record PaymentProviderGetResponse(

        String name,
        String configureUrl,
        String properties
) {
    public  static PaymentProviderGetResponse fromPaymentProvider(PaymentProvider paymentProvider){
        return new PaymentProviderGetResponse(
               paymentProvider.getName(),
                paymentProvider.getConfigureUrl(),paymentProvider.getProperties()
        );
    }
}
