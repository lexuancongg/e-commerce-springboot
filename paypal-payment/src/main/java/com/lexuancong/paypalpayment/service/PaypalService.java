package com.lexuancong.paypalpayment.service;

import com.lexuancong.paypalpayment.viewmodel.PaypalCreatePaymentRequest;
import com.lexuancong.paypalpayment.viewmodel.PaypalCreatePaymentResponse;
import com.paypal.core.PayPalHttpClient;
import com.paypal.orders.AmountWithBreakdown;
import com.paypal.orders.OrderRequest;
import com.paypal.orders.PurchaseUnitRequest;
import com.paypal.sdk.PaypalServerSdkClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaypalService {
    private final PayPalHttpClient payPalHttpClient;
    private final PaypalServerSdkClient paypalServerSdkClient;

    private final BigDecimal maxPay = new BigDecimal(100);

    public PaypalCreatePaymentResponse createPayment(PaypalCreatePaymentRequest paypalCreatePaymentRequest) {
        // đại diện cho data body trong req
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");
        BigDecimal totalPrice = paypalCreatePaymentRequest.totalPrice();
        // chia transaction ra nếu viwotj quá maxPay
        List<BigDecimal> transactions = new ArrayList<>();
        while (totalPrice.compareTo(BigDecimal.ZERO) > 0) {
            if(totalPrice.compareTo(maxPay) > 0) {
                totalPrice = totalPrice.subtract(maxPay);
                transactions.add(maxPay);
                continue;
            }
            transactions.add(totalPrice);
            totalPrice = BigDecimal.ZERO;

        }

        for (BigDecimal transaction : transactions) {
            PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest()
                    .amountWithBreakdown(
                            new AmountWithBreakdown()
                                    .currencyCode("USD")
                    );

        }



    }

}
