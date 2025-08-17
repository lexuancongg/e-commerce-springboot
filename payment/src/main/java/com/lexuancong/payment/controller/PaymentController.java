package com.lexuancong.payment.controller;

import com.lexuancong.payment.service.PaymentService;
import com.lexuancong.payment.viewmodel.InitPaymentRequest;
import com.lexuancong.payment.viewmodel.InitPaymentResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // khi nhấn nút thanh toán ... , khởi tạo một giao dịch
    @PostMapping("/init")
    public InitPaymentResponse initPayment (@RequestBody InitPaymentRequest initPaymentRequest ){
        return this.paymentService.initPayment(initPaymentRequest);
    }

}
