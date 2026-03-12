package com.lexuancong.address.controller;

import com.lexuancong.address.service.AddressService;
import com.lexuancong.address.dto.address.AddressDetailResponse;
import com.lexuancong.address.dto.address.AddressCreateRequest;
import com.lexuancong.address.dto.address.AddressResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AddressController{
    private final AddressService addressService;

    @PostMapping({"/internal-customer/addresses"})
    public ResponseEntity<AddressResponse> createAddress(
            @RequestBody @Valid AddressCreateRequest addressCreateRequest
    ){
        return ResponseEntity.ok(addressService.createAddress(addressCreateRequest));
    }




    @PutMapping({"/customer/addresses/{id}"})
    public ResponseEntity<Void> updateAddress(@PathVariable Long id,
                                              @RequestBody @Valid AddressCreateRequest addressCreateRequest){
        addressService.updateAddress(id, addressCreateRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping({"/customer/addresses/{id}","/internal-customer/addresses/{id}"})
    public ResponseEntity<AddressDetailResponse> getAddress(@PathVariable Long id){
        AddressDetailResponse addressDetailResponse = addressService.getAddressById(id);
        return ResponseEntity.ok(addressDetailResponse);
    }


    @GetMapping("/internal-customer/addresses")
    public ResponseEntity<List<AddressDetailResponse>> getAddresses(@RequestParam List<Long> ids){
        return ResponseEntity.ok(addressService.getAddresses(ids));
    }

    @DeleteMapping({"/internal-customer/addresses/{id}"})
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id){
        addressService.deleteAddress(id);
        return ResponseEntity.ok().build();
    }
}
