package com.lexuancong.customer.viewmodel.address;

public record AddressDetailVm(
        Long id,
        String contactName,
        String phoneNumber,
        String specificAddress,
        Long districtId,
        String districtName,
        Long provinceId,
        String provinceName,
        Long countryId,
        String countryName,
        boolean isActive
) {
}
