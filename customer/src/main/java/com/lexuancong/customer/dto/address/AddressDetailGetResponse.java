package com.lexuancong.customer.dto.address;

public record AddressDetailGetResponse(
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
