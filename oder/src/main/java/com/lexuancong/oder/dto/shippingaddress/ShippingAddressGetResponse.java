package com.lexuancong.oder.dto.shippingaddress;

import com.lexuancong.oder.model.ShippingAddress;

public record ShippingAddressGetResponse(
        Long id,
        String customerName,
        String phoneNumber,
        String specificAddress,
        Long districtId,
        Long provinceId,
        Long countryId
) {







    
    
    public static ShippingAddressGetResponse fromShippingAddress(ShippingAddress shippingAddress) {
        return new ShippingAddressGetResponse(
                shippingAddress.getId(),
                shippingAddress.getCustomerName(),
                shippingAddress.getPhoneNumber(),
                shippingAddress.getSpecificAddress(),
                shippingAddress.getDistrictId(),
                shippingAddress.getProvinceId(),
                shippingAddress.getCountyId()
                );
    }


}
