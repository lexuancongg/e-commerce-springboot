package com.lexuancong.customer.controller;

import com.lexuancong.customer.service.UserAddressService;
import com.lexuancong.customer.dto.address.AddressDetailGetResponse;
import com.lexuancong.customer.dto.address.AddressCreateRequest;
import com.lexuancong.customer.dto.useraddress.UserAddressGetResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class UserAddressController {
    private final UserAddressService userAddressService;
    // CHECKED
    @PostMapping("/customer/user-address")
    public ResponseEntity<UserAddressGetResponse> createUserAddress(@RequestBody @Valid AddressCreateRequest addressCreateRequest){
        return ResponseEntity.ok(userAddressService.createUserAddress(addressCreateRequest));

    }
   // CHECKED
    @GetMapping("/customer/user-address/default")
    public ResponseEntity<AddressDetailGetResponse>  getDefaultAddress(){
        return ResponseEntity.ok(userAddressService.getDefaultAddress());
    }

    // CHECKED
    @DeleteMapping("/customer/user-address/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id){
        userAddressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }


    // CHECKED
    @PutMapping("/customer/user-address/{id}")
    public ResponseEntity<Void> chooseDefaultAddress(@PathVariable Long id){
        userAddressService.chooseDefaultAddress(id);
        return ResponseEntity.ok().build();
    }


    // CHECKED
    // get danh sach address => cần tối ưu độ phức tạp thuật toán
    @GetMapping("/customer/user-address/addresses")
    public ResponseEntity<List<AddressDetailGetResponse>> getUserAddressDetail(){
        return ResponseEntity.ok(this.userAddressService.getUserAddressDetail());


    }


}
