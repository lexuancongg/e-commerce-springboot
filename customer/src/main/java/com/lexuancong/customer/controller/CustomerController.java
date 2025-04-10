package com.lexuancong.customer.controller;

import com.lexuancong.customer.service.CustomerService;
import com.lexuancong.customer.viewmodel.customer.CustomerPostVm;
import com.lexuancong.customer.viewmodel.customer.CustomerVm;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class CustomerController {
    private final CustomerService customerService;
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // api xem thông tin
    @GetMapping("/customer/customer/profile")
    public ResponseEntity<CustomerVm> getCustomerProfile() {
        return ResponseEntity.ok(customerService.getCustomerProfile());
    }

    @PostMapping("/customer/customer")
    public ResponseEntity<CustomerVm> createCustomer(@RequestBody @Valid CustomerPostVm customerPostVm,
                                                     UriComponentsBuilder uriComponentsBuilder) {
        CustomerVm customerVm = customerService.createCustomer(customerPostVm);
        return ResponseEntity.ok(customerVm);

    }


}
