package com.lexuancong.oder.viewmodel.shippingaddress;

import com.lexuancong.oder.model.ShippingAddress;

public record ShippingAddressVm(
        Long id,
        String customerName,
        String phoneNumber,
        String specificAddress,
        Long districtId,
        Long provinceId,
        Long countryId
) {







    
    
    public static ShippingAddressVm fromModel(ShippingAddress shippingAddress) {
        return new ShippingAddressVm(
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
