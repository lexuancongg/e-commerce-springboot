package com.lexuancong.payment.service.handler.providers;

import com.lexuancong.payment.model.InitiatedPaymentVm;
import com.lexuancong.payment.model.enumeration.PaymentMethod;
import com.lexuancong.payment.repository.PaymentProviderRepository;
import com.lexuancong.payment.dto.InitPaymentRequest;
import com.lexuancong.paypalpayment.service.PaypalService;
import com.lexuancong.paypalpayment.dto.PaypalCreatePaymentRequest;
import com.lexuancong.paypalpayment.dto.PaypalCreatePaymentResponse;
import org.springframework.stereotype.Service;

@Service
public class PaypalPaymentHandler extends AbstractPaymentProviderSupport implements ProviderPaymentHandler{
    private final PaypalService paypalService;
    public PaypalPaymentHandler(final PaymentProviderRepository paymentProviderRepository, PaypalService paypalService) {
        super(paymentProviderRepository);
        this.paypalService = paypalService;
    }
    @Override
    public String getNameProvider() {
        return PaymentMethod.PAYPAL.name();
    }

    @Override
    public InitiatedPaymentVm initPayment(InitPaymentRequest initPaymentRequest) {
        PaypalCreatePaymentRequest paypalCreatePaymentRequest = PaypalCreatePaymentRequest
                .builder()
                .orderId(initPaymentRequest.orderId())
                .providerPaymentSetting(this.getConfigurationProperties(this.getNameProvider()))
                .totalPrice(initPaymentRequest.totalPrice())
                .build();
        PaypalCreatePaymentResponse paypalCreatePaymentResponse = this.paypalService.createPayment(paypalCreatePaymentRequest);
        InitiatedPaymentVm initiatedPaymentVm = InitiatedPaymentVm.builder()
                .paymentId(paypalCreatePaymentResponse.paymentId())
                .redirectUrl(paypalCreatePaymentResponse.redirectUrl().split(",")[0])
                .status(paypalCreatePaymentResponse.status())
                .build();
        return null;
    }
}
