package com.lexuancong.payment.service.handler.providers;

import com.lexuancong.payment.dto.CapturePaymentRequest;
import com.lexuancong.payment.dto.CapturePaymentResponse;
import com.lexuancong.payment.dto.InitiatedPaymentResponse;
import com.lexuancong.payment.model.Payment;
import com.lexuancong.payment.model.enumeration.PaymentMethod;
import com.lexuancong.payment.model.enumeration.PaymentStatus;
import com.lexuancong.payment.repository.PaymentProviderRepository;
import com.lexuancong.payment.dto.InitPaymentRequest;
import com.lexuancong.payment.repository.PaymentRepository;
import com.lexuancong.paypalpayment.dto.PaypalCaptureRequest;
import com.lexuancong.paypalpayment.dto.PaypalCaptureResponse;
import com.lexuancong.paypalpayment.service.PaypalService;
import com.lexuancong.paypalpayment.dto.PaypalCreatePaymentRequest;
import com.lexuancong.paypalpayment.dto.PaypalCreatePaymentResponse;
import org.springframework.stereotype.Service;

@Service
public class PaypalPaymentHandler extends AbstractPaymentProviderSupport implements ProviderPaymentHandler{
    private final PaypalService paypalService;

    public PaypalPaymentHandler(final PaymentProviderRepository paymentProviderRepository, PaymentRepository paymentRepository, PaypalService paypalService) {
        super(paymentProviderRepository);
        this.paypalService = paypalService;
    }
    @Override
    public String getNameProvider() {
        return PaymentMethod.PAYPAL.name();
    }

    @Override
    public InitiatedPaymentResponse initPayment(InitPaymentRequest initPaymentRequest) {
        PaypalCreatePaymentRequest paypalCreatePaymentRequest = PaypalCreatePaymentRequest
                .builder()
                .orderId(initPaymentRequest.orderId())
                .totalPrice(initPaymentRequest.totalPrice())
                .build();
        PaypalCreatePaymentResponse paypalCreatePaymentResponse = this.paypalService.createPayment(paypalCreatePaymentRequest);
        InitiatedPaymentResponse initiatedPaymentResponse = InitiatedPaymentResponse.builder()
                .redirectUrl(paypalCreatePaymentResponse.redirectUrl())
                .status(paypalCreatePaymentResponse.status())
                .build();
        return initiatedPaymentResponse;
    }

    @Override
    public CapturePaymentResponse capturePayment(CapturePaymentRequest capturePaymentRequest) {
        PaypalCaptureRequest captureRequest = PaypalCaptureRequest.builder()
                .token(capturePaymentRequest.token())
                .orderId(capturePaymentRequest.orderId())
                .paymentId(capturePaymentRequest.paymentId())
                .build();
        PaypalCaptureResponse captureResponse = this.paypalService.capturePayment(captureRequest);


        return   CapturePaymentResponse.builder()
                .paymentId(captureResponse.paymentId())
                .status(PaymentStatus.valueOf(captureResponse.status()))
                .build();

    }
}
