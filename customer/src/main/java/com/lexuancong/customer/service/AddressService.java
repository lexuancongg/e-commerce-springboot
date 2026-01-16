package com.lexuancong.customer.service;

import com.lexuancong.customer.dto.address.AddressDetailGetResponse;
import com.lexuancong.customer.dto.address.AddressCreateRequest;
import com.lexuancong.customer.dto.address.AddressGetResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    public AddressGetResponse createAddress(AddressCreateRequest addressCreateRequest){
        return null;
    }

    public void deleteAddress(Long id){

    }

    public AddressDetailGetResponse getAddressById(Long id){
        return null;
    }
    public List<AddressGetResponse> getAddressesByIds(List<Long> ids){return  null;}

}
