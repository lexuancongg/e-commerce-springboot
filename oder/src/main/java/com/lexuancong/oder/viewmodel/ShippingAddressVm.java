package com.lexuancong.oder.viewmodel;

public record ShippingAddressVm(
        Long id,
        String customerName,
        String phoneNumber,
        String specificAddress,
        Long districtId,
        Long provinceId,
        Long countryId


) {
}
