package com.lexuancong.address.viewmodel.address;

import com.lexuancong.address.model.Address;
import lombok.Builder;

@Builder
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
        String countryName
) {
    public static AddressDetailVm fromModel(final Address address) {
        return AddressDetailVm.builder()
                .id(address.getId())
                .contactName(address.getContactName())
                .phoneNumber(address.getPhoneNumber())
                .specificAddress(address.getSpecificAddress())
                .districtId(address.getDistrict().getId())
                .districtName(address.getDistrict().getName())
                .provinceId(address.getProvince().getId())
                .provinceName(address.getProvince().getName())
                .countryId(address.getCountry().getId())
                .countryName(address.getCountry().getName())
                .build();
    }
}

