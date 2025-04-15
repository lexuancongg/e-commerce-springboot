package com.lexuancong.customer.mapper;

import com.lexuancong.customer.model.UserAddress;
import com.lexuancong.customer.viewmodel.address.AddressVm;
import com.lexuancong.customer.viewmodel.useraddress.UserAddressVm;
import org.springframework.stereotype.Component;

@Component
public class UserAddressMapper {
    public UserAddressVm toVmFromModel(UserAddress userAddress, AddressVm addressVm) {
        return UserAddressVm.builder()
                .id(userAddress.getId())
                .userId(userAddress.getUserId())
                .addressVm(addressVm)
                .isActive(userAddress.isActive())
                .build();
    }
}
