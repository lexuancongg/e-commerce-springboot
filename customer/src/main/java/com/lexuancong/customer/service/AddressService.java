package com.lexuancong.customer.service;

import com.lexuancong.customer.viewmodel.address.AddressDetailVm;
import com.lexuancong.customer.viewmodel.address.AddressPostVm;
import com.lexuancong.customer.viewmodel.address.AddressVm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    public AddressVm createAddress(AddressPostVm addressPostVm){
        return null;
    }

    public AddressDetailVm getAddressById(Long id){
        return null;
    }
    public List<AddressVm> getAddressesByIds(List<Long> ids){return  null;}

}
