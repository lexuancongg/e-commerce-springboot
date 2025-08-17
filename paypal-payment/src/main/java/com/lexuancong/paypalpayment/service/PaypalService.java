package com.lexuancong.paypalpayment.service;

import com.lexuancong.paypalpayment.viewmodel.PaypalCreatePaymentRequest;
import com.lexuancong.paypalpayment.viewmodel.PaypalCreatePaymentResponse;
import com.paypal.core.PayPalHttpClient;
import com.paypal.orders.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaypalService {
    private final PayPalHttpClient payPalHttpClient;

    public PaypalCreatePaymentResponse createPayment(PaypalCreatePaymentRequest paypalCreatePaymentRequest) {
        OrderRequest orderRequest = new OrderRequest();

    }

}
