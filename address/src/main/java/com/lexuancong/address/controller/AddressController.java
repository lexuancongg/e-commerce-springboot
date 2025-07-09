package com.lexuancong.address.controller;

import com.lexuancong.address.service.AddressService;
import com.lexuancong.address.viewmodel.address.AddressDetailVm;
import com.lexuancong.address.viewmodel.address.AddressPostVm;
import com.lexuancong.address.viewmodel.address.AddressGetVm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AddressController{
    private final AddressService addressService;

    @PostMapping("/customer/address")
    public ResponseEntity<AddressGetVm> createAddress(@RequestBody @Valid AddressPostVm addressPostVm){
        return ResponseEntity.ok(addressService.createAddress(addressPostVm));
    }

    @PutMapping("/customer/address/{id}")
    public ResponseEntity<Void> updateAddress(@PathVariable Long id, @RequestBody @Valid AddressPostVm addressPostVm){
        addressService.updateAddress(id,addressPostVm);
        return ResponseEntity.ok().build();
    }

    @GetMapping("customer/address/{id}")
    public ResponseEntity<AddressDetailVm> getAddress(@PathVariable Long id){
        AddressDetailVm addressDetailVm = addressService.getAddressById(id);
        return ResponseEntity.ok(addressDetailVm);
    }

    @GetMapping("/customer/addresses")
    public ResponseEntity<List<AddressDetailVm>> getAddresses(@RequestParam List<Long> ids){
        return ResponseEntity.ok(addressService.getAddresses(ids));
    }

    @DeleteMapping("/customer/address/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id){
        addressService.deleteAddress(id);
        return ResponseEntity.ok().build();
    }
}
