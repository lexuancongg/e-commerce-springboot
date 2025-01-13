package com.lexuancong.customer.viewmodel.address;

public record AddressDetailVm(
        Long id,
        String contactName,
        String phone,
        String addressLine,
        Long districtId,
        String districtName,
        Long stateOrProvinceId,
        String stateOrProvinceName,
        Long countryId,
        String countryName
) {
}
