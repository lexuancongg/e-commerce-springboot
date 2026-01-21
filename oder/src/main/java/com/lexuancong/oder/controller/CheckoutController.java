package com.lexuancong.oder.controller;

import com.lexuancong.oder.service.CheckoutService;
import com.lexuancong.oder.dto.checkout.CheckoutCreateRequest;
import com.lexuancong.oder.dto.checkout.CheckoutGetResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CheckoutController {
    private final CheckoutService checkoutService;
    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }
    @PostMapping("customer/checkouts")
    public ResponseEntity<CheckoutGetResponse> createCheckout(@Valid @RequestBody CheckoutCreateRequest checkoutCreateRequest) {
        return ResponseEntity.ok(this.checkoutService.createCheckout(checkoutCreateRequest));
    }

    @GetMapping("/customer/checkouts/{id}")
    public ResponseEntity<CheckoutGetResponse> getCheckoutById(@PathVariable Long id) {
        return ResponseEntity.ok(this.checkoutService.getCheckoutById(id));

    }




}
