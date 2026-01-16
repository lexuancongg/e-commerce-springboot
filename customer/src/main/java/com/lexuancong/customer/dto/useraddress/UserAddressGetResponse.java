package com.lexuancong.customer.dto.useraddress;

import com.lexuancong.customer.model.UserAddress;
import com.lexuancong.customer.dto.address.AddressGetResponse;
import lombok.Builder;

@Builder
public record UserAddressGetResponse(
        Long id,
        String userId,
        // object chua thong tin dia chi
        AddressGetResponse addressGetResponse,
        boolean isActive
){

    public static UserAddressGetResponse fromUserAddress(UserAddress userAddress, AddressGetResponse addressGetResponse) {
        return new UserAddressGetResponse(
                userAddress.getId(),
                userAddress.getUserId(),
                addressGetResponse,
                userAddress.isActive()
        );
    }
}
