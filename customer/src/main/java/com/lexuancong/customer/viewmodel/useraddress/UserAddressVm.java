package com.lexuancong.customer.viewmodel.useraddress;

import com.lexuancong.customer.model.UserAddress;
import com.lexuancong.customer.viewmodel.address.AddressVm;
import lombok.Builder;

@Builder
public record UserAddressVm(
        Long id,
        String userId,
        // object chua thong tin dia chi
        AddressVm addressVm,
        boolean isActive
){

    public static UserAddressVm fromModel(UserAddress userAddress, AddressVm addressVm) {
        return new UserAddressVm(
                userAddress.getId(),
                userAddress.getUserId(),
                addressVm,
                userAddress.isActive()
        );
    }
}
