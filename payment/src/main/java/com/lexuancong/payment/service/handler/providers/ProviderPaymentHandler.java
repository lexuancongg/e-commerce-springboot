package com.lexuancong.payment.service.handler.providers;

import com.lexuancong.payment.dto.CapturePaymentRequest;
import com.lexuancong.payment.dto.CapturePaymentResponse;
import com.lexuancong.payment.dto.InitiatedPaymentResponse;
import com.lexuancong.payment.dto.InitPaymentRequest;

// sau này có nhiều dịch vụ thanh toán thứ 3 => chúng ta sẽ lấy một lớp tong quat dai dien cho all
public interface ProviderPaymentHandler {
     final String PAYMENT_COMPLETED="COMPLETED";
     String getNameProvider();
     InitiatedPaymentResponse initPayment(InitPaymentRequest initPaymentRequest);
     CapturePaymentResponse capturePayment(CapturePaymentRequest capturePaymentRequest);
}
