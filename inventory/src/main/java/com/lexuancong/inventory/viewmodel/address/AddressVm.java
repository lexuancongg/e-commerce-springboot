package com.lexuancong.inventory.viewmodel.address;

public record AddressVm(
        Long id,
        String contactName,
        String phoneNumber,
        String specificAddress,
        Long districtId,
        Long provinceId,
        Long countryId
) {
}
