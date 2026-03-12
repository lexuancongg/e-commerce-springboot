package com.lexuancong.customer.dto.useraddress;

import com.lexuancong.customer.model.UserAddress;
import com.lexuancong.customer.dto.address.AddressResponse;
import lombok.Builder;

@Builder
public record UserAddressResponse(
        Long id,
        String userId,
        AddressResponse address,
        boolean isActive
){

    public static UserAddressResponse fromUserAddress(UserAddress userAddress, AddressResponse addressResponse) {
        return new UserAddressResponse(
                userAddress.getId(),
                userAddress.getUserId(),
                addressResponse,
                userAddress.isActive()
        );
    }
}
