package com.lexuancong.payment.controller;

import com.lexuancong.payment.service.PaymentService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

}
