package com.lexuancong.paypalpayment.service;

import com.lexuancong.paypalpayment.dto.PaypalCaptureRequest;
import com.lexuancong.paypalpayment.dto.PaypalCaptureResponse;
import com.lexuancong.paypalpayment.dto.PaypalCreatePaymentRequest;
import com.lexuancong.paypalpayment.dto.PaypalCreatePaymentResponse;
import com.lexuancong.share.exception.NotFoundException;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import com.paypal.sdk.PaypalServerSdkClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaypalService {
    private final PayPalHttpClient payPalHttpClient;
    // cách dùng cũ => tham khảo sau
    private final PaypalServerSdkClient paypalServerSdkClient;
    @Value("${}")
    private  String returnUrl;
    @Value("${}")
    private  String cancelUrl;

    private final String brand = "lexuancong";

    public PaypalCreatePaymentResponse createPayment(PaypalCreatePaymentRequest request) {
        PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest()
                .amountWithBreakdown(
                        new AmountWithBreakdown()
                                .currencyCode("USD")
                                .value(request.totalPrice().toString())
                );

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");
        orderRequest.purchaseUnits(List.of(purchaseUnitRequest));
        ApplicationContext applicationContext = new ApplicationContext()
                .returnUrl(this.returnUrl)
                .cancelUrl(this.cancelUrl)
                .brandName(this.brand)
                .landingPage("BILLING")
                .userAction("PAY_NOW")
                .shippingPreference("NO_SHIPPING");

        orderRequest.applicationContext(applicationContext);

        OrdersCreateRequest ordersCreateRequest = (OrdersCreateRequest) new OrdersCreateRequest()
                .header("prefer", "return=representation")
                .requestBody(orderRequest);

        try {
            HttpResponse<Order> response = this.payPalHttpClient.execute(ordersCreateRequest);
            Order order = response.result();

            String redirectUrl = order.links().stream()
                    .filter(link -> "approve".equals(link.rel()))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Approve URL not found"))
                    .href();

            return new PaypalCreatePaymentResponse(HttpStatus.OK.name(), request.orderId(), redirectUrl);

        } catch (IOException e) {
            e.printStackTrace();
            return new PaypalCreatePaymentResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(), request.orderId(), null);
        }
    }


    public PaypalCaptureResponse capturePayment(PaypalCaptureRequest request) {
        OrdersCaptureRequest ordersCaptureRequest = new OrdersCaptureRequest(request.token());
        try {
            HttpResponse<Order> httpResponse = payPalHttpClient.execute(ordersCaptureRequest);
            Order order = httpResponse.result();

            boolean isSuccess = "COMPLETED".equals(order.status());

            return PaypalCaptureResponse.builder()
                    .paymentId(request.paymentId())
                    .status(isSuccess ? "COMPLETED" : "FAILED")
                    .build();

        } catch (IOException e) {
            return PaypalCaptureResponse.builder()
                    .paymentId(request.paymentId())
                    .status("FAILED")
                    .build();
        }

    }

}


// docs
// https://developer.paypal.com/studio/checkout/standard/integrate
// https://developer.paypal.com/serversdk/java/api-endpoints/orders/create-order
//  https://developer.paypal.com/docs/api/orders/sdk/v2/#orders_create
// https://bitshifted.co/blog/spring-boot-paypal-integration/