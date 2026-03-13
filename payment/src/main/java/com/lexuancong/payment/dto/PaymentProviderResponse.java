package com.lexuancong.payment.dto;

import com.lexuancong.payment.model.PaymentProvider;

public record PaymentProviderResponse(

        String name,
        String properties
) {
    public  static PaymentProviderResponse fromPaymentProvider(PaymentProvider paymentProvider){
        return new PaymentProviderResponse(
               paymentProvider.getName(),
                paymentProvider.getProperties()
        );
    }
}
