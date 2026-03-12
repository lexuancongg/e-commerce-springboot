package com.lexuancong.customer.dto.address;

public record AddressResponse(
        Long id ,
        String contactName,
        String phoneNumber,
        String specificAddress,
        Long districtId,
        String districtName,
        Long provinceId,
        String provinceName,
        Long countryId,
        String countryName
) {
}
