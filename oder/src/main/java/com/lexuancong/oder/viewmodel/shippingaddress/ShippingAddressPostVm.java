package com.lexuancong.oder.viewmodel.shippingaddress;

import com.lexuancong.oder.model.ShippingAddress;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ShippingAddressPostVm(
        @NotBlank String customerName,
        @NotBlank String phoneNumber,
        @NotBlank String specificAddress ,
        @NotNull Long districtId,
        @NotNull Long provinceId,
        @NotNull Long countryId

) {
    public ShippingAddress toModel() {
        return ShippingAddress.builder()
                .customerName(customerName)
                .phoneNumber(phoneNumber)
                .specificAddress(specificAddress)
                .districtId(districtId)
                .provinceId(provinceId)
                .countyId(countryId)
                .build();


    }
}
