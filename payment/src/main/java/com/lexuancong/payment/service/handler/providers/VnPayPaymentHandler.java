package com.lexuancong.payment.service.handler.providers;

import com.lexuancong.payment.dto.InitiatedPaymentResponse;
import com.lexuancong.payment.model.enumeration.PaymentMethod;
import com.lexuancong.payment.repository.PaymentProviderRepository;
import com.lexuancong.payment.dto.InitPaymentRequest;
import com.lexuancong.vnpaypayment.service.VnpayService;
import com.lexuancong.vnpaypayment.dto.VnpayCreatePaymentUrlRequest;
import org.springframework.stereotype.Component;

@Component
public class VnPayPaymentHandler extends AbstractPaymentProviderSupport implements ProviderPaymentHandler  {
    private final VnpayService vnpayService;

    public VnPayPaymentHandler(VnpayService vnpayService , PaymentProviderRepository paymentProviderRepository) {
        super(paymentProviderRepository);
        this.vnpayService = vnpayService;
    }

    @Override
    public String getNameProvider() {
        return PaymentMethod.VNPAY.name();
    }

    // đoạn code xử lý dành cho vn pay
    @Override
    public InitiatedPaymentResponse initPayment(InitPaymentRequest initPaymentRequest) {
        try {
            VnpayCreatePaymentUrlRequest vnpayCreatePaymentUrlRequest = new VnpayCreatePaymentUrlRequest(
                    initPaymentRequest.totalPrice(),
                    initPaymentRequest.paymentMethod(),
                    this.getConfigurationProperties(this.getNameProvider()),
                    ""
            );

            String vnp_paymentUrl = vnpayService.createPaymentUrl(vnpayCreatePaymentUrlRequest);
            return  InitiatedPaymentResponse.builder()
                    .paymentId(null)
                    .provider(PaymentMethod.VNPAY.name())
                    .redirectUrl(vnp_paymentUrl)
                    .status("PENDING")
                    .build();

        }catch (Exception e){
            System.out.println("error");
            return null;
        }

    }
}
