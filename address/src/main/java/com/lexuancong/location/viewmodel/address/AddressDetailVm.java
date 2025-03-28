package com.lexuancong.location.viewmodel.address;

import com.lexuancong.location.model.Address;
import lombok.Builder;

@Builder
public record AddressDetailVm(
        Long id,
        String contactName,
        String phone,
        String addressLine,
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
                .phone(address.getPhoneNumber())
                .addressLine(address.getSpecificAddress())
                .districtId(address.getDistrict().getId())
                .districtName(address.getDistrict().getName())
                .provinceId(address.getProvince().getId())
                .provinceName(address.getProvince().getName())
                .countryId(address.getCountry().getId())
                .countryName(address.getCountry().getName())
                .build();
    }
}

