package com.lexuancong.customer.mapper;

import com.lexuancong.customer.model.CustomerAddress;
import com.lexuancong.customer.viewmodel.address.AddressVm;
import com.lexuancong.customer.viewmodel.useraddress.UserAddressVm;
import org.springframework.stereotype.Component;

@Component
public class UserAddressMapper {
    public UserAddressVm toVmFromModel(CustomerAddress customerAddress, AddressVm addressVm) {
        return UserAddressVm.builder()
                .id(customerAddress.getId())
                .userId(customerAddress.getUserId())
                .addressVm(addressVm)
                .isActive(customerAddress.isActive())
                .build();
    }
}
