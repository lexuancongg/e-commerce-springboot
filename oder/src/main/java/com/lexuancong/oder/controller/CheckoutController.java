package com.lexuancong.oder.controller;

import com.lexuancong.oder.service.CheckoutService;
import com.lexuancong.oder.viewmodel.checkout.CheckoutPostVm;
import com.lexuancong.oder.viewmodel.checkout.CheckoutVm;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckoutController {
    private final CheckoutService checkoutService;
    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }
    @PostMapping("customer/checkouts")
    public ResponseEntity<CheckoutVm> createCheckout(@Valid @RequestBody CheckoutPostVm checkoutPostVm) {
        return ResponseEntity.ok(this.checkoutService.createCheckout(checkoutPostVm));
    }


}
