package com.lexuancong.paypalpayment.service;

import com.lexuancong.paypalpayment.viewmodel.PaypalCreatePaymentRequest;
import com.lexuancong.paypalpayment.viewmodel.PaypalCreatePaymentResponse;
import com.lexuancong.share.exception.NotFoundException;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import com.paypal.sdk.PaypalServerSdkClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaypalService {
    private final PayPalHttpClient payPalHttpClient;
    private final PaypalServerSdkClient paypalServerSdkClient;
    @Value("${}")
    private  String returnUrl;
    @Value("${}")
    private  String cancelUrl;

    private final String brandName = "lexuancong";

    private final BigDecimal maxPay = new BigDecimal(100);

    public PaypalCreatePaymentResponse createPayment(PaypalCreatePaymentRequest paypalCreatePaymentRequest) {
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

        List<String> redirectUrls = new ArrayList<>();

        for (BigDecimal amount : transactions) {
            PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest()
                    .amountWithBreakdown(
                            new AmountWithBreakdown()
                                    .currencyCode("USD")
                                    .value(amount.toString())
                    );
            // đại diện cho data body trong req
            OrderRequest orderRequest = new OrderRequest();
            orderRequest.checkoutPaymentIntent("CAPTURE");
            orderRequest.purchaseUnits(List.of(purchaseUnitRequest));
            // câu hiình giao diện khi thanh toán treen paypal
            ApplicationContext applicationContext = new ApplicationContext()
                    .returnUrl(this.returnUrl)
                    .cancelUrl(this.cancelUrl)
                    .brandName(this.brandName)
                    .landingPage("BILLING")
                    .userAction("PAY_NOW")
                    .shippingPreference("NO_SHIPPING");

            orderRequest.applicationContext(applicationContext);
            OrdersCreateRequest request = (OrdersCreateRequest) new OrdersCreateRequest()
                    .header("perfer","return=representation")
                    .requestBody(orderRequest);
            try {
                HttpResponse<Order> orderHttpResponse = this.payPalHttpClient.execute(request);
                Order order = orderHttpResponse.result();
                String redirectUrl  = order.links().stream()
                        .filter(link -> "approve".equals(link))
                        .findFirst()
                        .orElseThrow(()-> new NotFoundException(""))
                        .href();

                redirectUrls.add(redirectUrl);
                return new PaypalCreatePaymentResponse(HttpStatus.OK.name(),paypalCreatePaymentRequest.orderId(),redirectUrl);

            }catch (IOException exception){
                exception.printStackTrace();
                return new PaypalCreatePaymentResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(),paypalCreatePaymentRequest.orderId(),null);
            }




        }
        return new PaypalCreatePaymentResponse(
                HttpStatus.OK.name(),
                paypalCreatePaymentRequest.orderId(),
                String.join(",", redirectUrls)
        );

    }

}


// docs
// https://developer.paypal.com/studio/checkout/standard/integrate
// https://developer.paypal.com/serversdk/java/api-endpoints/orders/create-order
//  https://developer.paypal.com/docs/api/orders/sdk/v2/#orders_create