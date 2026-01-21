package com.lexuancong.oder.dto.shippingaddress;

import com.lexuancong.oder.model.ShippingAddress;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ShippingAddressCreateRequest(
        @NotBlank String customerName,
        @NotBlank String phoneNumber,
        @NotBlank String specificAddress ,
        @NotNull Long districtId,
        @NotNull Long provinceId,
        @NotNull Long countryId

) {
    public ShippingAddress toShippingAddress() {
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
