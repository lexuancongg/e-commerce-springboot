package com.lexuancong.customer.controller;

import com.lexuancong.customer.service.CustomerService;
import com.lexuancong.customer.viewmodel.customer.CustomerPostVm;
import com.lexuancong.customer.viewmodel.customer.CustomerProfilePutVm;
import com.lexuancong.customer.viewmodel.customer.CustomerVm;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    // sửa lại sau vì keycloak tự tạo user cho
    @PostMapping("/customer/customer")
    public ResponseEntity<CustomerVm> createCustomer(@RequestBody @Valid CustomerPostVm customerPostVm,
                                                     UriComponentsBuilder uriComponentsBuilder) {
        CustomerVm customerVm = customerService.createCustomer(customerPostVm);
        return ResponseEntity.ok(customerVm);

    }
    @PutMapping("/customer/customer/profile")
    public ResponseEntity<Void> updateCustomerProfile(@RequestBody CustomerProfilePutVm customerProfilePutVm){
        this.customerService.updateCustomerProfile(customerProfilePutVm);
        return ResponseEntity.noContent().build();
    }


}
