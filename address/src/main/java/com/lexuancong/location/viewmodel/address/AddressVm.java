package com.lexuancong.location.viewmodel.address;

import com.lexuancong.location.model.Address;
import lombok.Builder;

@Builder
public record AddressVm(
        Long id,
        String contactName,
        String phoneNumber,
        String specificAddress,
        Long districtId,
        Long provinceId,
        Long countryId
) {
    public  static AddressVm fromModel(Address address){
        return  AddressVm.builder()
                .id(address.getId())
                .contactName(address.getContactName())
                .phoneNumber(address.getPhoneNumber())
                .specificAddress(address.getSpecificAddress())
                .districtId(address.getDistrict().getId())
                .provinceId(address.getProvince().getId())
                .countryId(address.getCountry().getId())
                .build();
    }
}


