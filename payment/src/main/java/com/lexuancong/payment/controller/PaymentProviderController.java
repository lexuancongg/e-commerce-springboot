package com.lexuancong.payment.controller;

import com.lexuancong.payment.service.PaymentProviderService;
import com.lexuancong.payment.dto.PaymentProviderGetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PaymentProviderController {
    private final PaymentProviderService paymentProviderService;



    // get các method payment cho người dùng chọn
    @GetMapping("/customer/payment-providers")
    public ResponseEntity<List<PaymentProviderGetResponse>> getPaymentProviders(){
        return ResponseEntity.ok(this.paymentProviderService.getPaymentProviders());
    }

}
