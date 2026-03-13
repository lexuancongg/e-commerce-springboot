package com.lexuancong.payment.controller;

import com.lexuancong.payment.dto.CapturePaymentRequest;
import com.lexuancong.payment.dto.CapturePaymentResponse;
import com.lexuancong.payment.service.PaymentService;
import com.lexuancong.payment.dto.InitPaymentRequest;
import com.lexuancong.payment.dto.InitPaymentResponse;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService)
    {
        this.paymentService = paymentService;
    }

    @PostMapping("/init")
    public ResponseEntity<InitPaymentResponse> initPayment (@RequestBody InitPaymentRequest initPaymentRequest ){
        return ResponseEntity.ok(paymentService.initPayment(initPaymentRequest));
    }

    @PostMapping("/capture")
    public ResponseEntity<CapturePaymentResponse> capturePayment(
            @RequestBody @Valid CapturePaymentRequest capturePaymentRequest
    ){
        return ResponseEntity.ok(
                paymentService.capturePayment(capturePaymentRequest)
        );
    }

}
