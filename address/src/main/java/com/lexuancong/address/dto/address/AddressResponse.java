package com.lexuancong.address.dto.address;

import com.lexuancong.address.model.Address;
import lombok.Builder;

@Builder
public record AddressResponse(
        Long id,
        String contactName,
        String phoneNumber,
        String specificAddress,
        Long districtId,
        Long provinceId,
        Long countryId
) {
    public  static AddressResponse fromAddress(Address address){
        return  AddressResponse.builder()
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
