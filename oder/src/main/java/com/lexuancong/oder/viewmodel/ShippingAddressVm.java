package com.lexuancong.oder.viewmodel;

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


}
