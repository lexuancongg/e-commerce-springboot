package com.lexuancong.customer.viewmodel.address;

public record AddressVm(Long id , String contactName,String phone,String addressLine,
                        Long districtId,Long stateOrProvinceId,Long countryId) {
}
