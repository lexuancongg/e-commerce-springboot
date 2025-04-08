package com.lexuancong.customer.controller;

import com.lexuancong.customer.service.CustomerAddressService;
import com.lexuancong.customer.viewmodel.address.AddressDetailVm;
import com.lexuancong.customer.viewmodel.address.AddressPostVm;
import com.lexuancong.customer.viewmodel.useraddress.UserAddressVm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/customer")
@RestController
@RequiredArgsConstructor
public class CustomerAddressController {
    private final CustomerAddressService customerAddressService;
    // api add address
    @PostMapping("/customer/user-address")
    public ResponseEntity<UserAddressVm> createAddress(@RequestBody @Valid AddressPostVm addressPostVm){
        return ResponseEntity.ok(customerAddressService.createAddress(addressPostVm));

    }
    // api get  address default
    @GetMapping("/customer/user-address/default")
    public ResponseEntity<AddressDetailVm>  getDefaultAddress(){
        return ResponseEntity.ok(customerAddressService.getDefaultAddress());
    }

    // delete address
    @DeleteMapping("/customer/user-address/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id){
        customerAddressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/customer/user-address/{id}")
    public ResponseEntity<Void> chooseDefaultAddress(@PathVariable Long id){
        customerAddressService.chooseDefaultAddress(id);
        return ResponseEntity.ok().build();
    }


    // get danh sach address
    @GetMapping("/customer/addresses")
    public ResponseEntity<List<AddressDetailVm>> getDetailAddresses(){
        return ResponseEntity.ok(this.customerAddressService.getDetailAddresses());


    }


}
