package com.lexuancong.oder.dto.shippingaddress;

import com.lexuancong.oder.model.ShippingAddress;

public record ShippingAddressResponse(
        Long id,
        String customerName,
        String phoneNumber,
        String specificAddress,
        Long districtId,
        Long provinceId,
        Long countryId
) {







    
    
    public static ShippingAddressResponse fromShippingAddress(ShippingAddress shippingAddress) {
        return new ShippingAddressResponse(
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
