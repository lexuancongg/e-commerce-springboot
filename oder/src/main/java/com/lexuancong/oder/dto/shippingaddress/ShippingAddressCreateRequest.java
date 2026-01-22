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
        @NotNull Long countryId,
        @NotBlank String countryName,
        @NotBlank String provinceName,
        @NotBlank String districtName

) {
    public ShippingAddress toShippingAddress() {
        return ShippingAddress.builder()
                .customerName(customerName)
                .phoneNumber(phoneNumber)
                .specificAddress(specificAddress)
                .districtId(districtId)
                .provinceId(provinceId)
                .countyId(countryId)
                .countryName(countryName)
                .provinceName(provinceName)
                .districtName(districtName)
                .build();


    }
}
