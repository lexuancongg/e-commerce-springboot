package com.lexuancong.customer.controller;

import com.lexuancong.customer.service.CustomerService;
import com.lexuancong.customer.dto.customer.CustomerResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomerController {
    private final CustomerService customerService;
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // đã check
    @GetMapping({"/customer/customers/profile","/internal-feedback/customers/profile"})
    public ResponseEntity<CustomerResponse> getCustomerProfile() {
        return ResponseEntity.ok(customerService.getCustomerProfile());
    }




}
