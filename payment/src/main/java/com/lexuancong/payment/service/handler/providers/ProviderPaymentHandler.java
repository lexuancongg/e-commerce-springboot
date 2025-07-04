package com.lexuancong.payment.service.handler.providers;

import com.lexuancong.payment.model.InitiatedPayment;
import com.lexuancong.payment.viewmodel.InitPaymentRequest;

// sau này có nhiều dịch vụ thanh toán thứ 3 => chúng ta sẽ lấy một lớp tong quat dai dien cho all
public interface ProviderPaymentHandler {
     String getNameProvider();
     InitiatedPayment initPayment(InitPaymentRequest initPaymentRequest);
}
