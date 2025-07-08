package com.lexuancong.payment.service.handler.providers;

import com.lexuancong.payment.model.InitiatedPayment;
import com.lexuancong.payment.model.enumeration.PaymentMethod;
import com.lexuancong.payment.repository.PaymentProviderRepository;
import com.lexuancong.payment.viewmodel.InitPaymentRequest;
import com.lexuancong.vnpaypayment.service.VnpayService;
import com.lexuancong.vnpaypayment.viewmodel.VnpayCreatePaymentUrlRequest;
import org.springframework.stereotype.Component;

@Component
// vnpay provider
public class VnPayProviderPaymentHandler extends AbstractPaymentProviderSupport implements ProviderPaymentHandler  {
    private final VnpayService vnpayService;

    public VnPayProviderPaymentHandler(VnpayService vnpayService , PaymentProviderRepository paymentProviderRepository) {
        super(paymentProviderRepository);
        this.vnpayService = vnpayService;
    }

    @Override
    public String getNameProvider() {
        return PaymentMethod.VNPAY.name();
    }

    // đoạn code xử lý dành cho vn pay
    @Override
    public InitiatedPayment initPayment(InitPaymentRequest initPaymentRequest) {
        try {
            VnpayCreatePaymentUrlRequest vnpayCreatePaymentUrlRequest = new VnpayCreatePaymentUrlRequest(
                    initPaymentRequest.totalPrice(),
                    initPaymentRequest.paymentMethod(),
                    this.getConfigurationProperties(this.getNameProvider())
            );

            String vnp_paymentUrl = vnpayService.createPaymentUrl(vnpayCreatePaymentUrlRequest);
            return  InitiatedPayment.builder()
                    .paymentId(null)
                    .provider("VNPAY")
                    .redirectUrl(vnp_paymentUrl)
                    .status("PENDING")
                    .build();

        }catch (Exception e){
            System.out.println("error");
        }
    }
}
