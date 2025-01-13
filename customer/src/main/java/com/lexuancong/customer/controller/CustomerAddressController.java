package com.lexuancong.customer.controller;

import com.lexuancong.customer.service.UserAddressService;
import com.lexuancong.customer.viewmodel.address.AddressDetailVm;
import com.lexuancong.customer.viewmodel.address.AddressPostVm;
import com.lexuancong.customer.viewmodel.useraddress.UserAddressVm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CustomerAddressController {
    private final UserAddressService userAddressService;
    // api add address
    @PostMapping("/storefront/user-address")
    public ResponseEntity<UserAddressVm> createAddress(@RequestBody @Valid AddressPostVm addressPostVm){
        return ResponseEntity.ok(userAddressService.createAddress(addressPostVm));

    }
    // api get  address default
    @GetMapping("/storefront/user-address/default")
    public ResponseEntity<AddressDetailVm>  getDefaultAddress(){
        return ResponseEntity.ok(userAddressService.getDefaultAddress());
    }

    // delete address
    @DeleteMapping("/storefront/user-address/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id){
        userAddressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/storefront/user-address/{id}")
    public ResponseEntity<Void> chooseDefaultAddress(@PathVariable Long id){
        userAddressService.chooseDefaultAddress(id);
        return ResponseEntity.ok().build();
    }


}
