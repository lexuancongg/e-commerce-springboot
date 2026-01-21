package com.lexuancong.inventory.dto.address;

public record AddressGetResponse(
        Long id,
        String contactName,
        String phoneNumber,
        String specificAddress,
        Long districtId,
        Long provinceId,
        Long countryId
) {
}
