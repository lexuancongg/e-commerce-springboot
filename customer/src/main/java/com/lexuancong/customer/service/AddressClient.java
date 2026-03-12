package com.lexuancong.customer.service;

import com.lexuancong.customer.dto.address.AddressDetailResponse;
import com.lexuancong.customer.dto.address.AddressCreateRequest;
import com.lexuancong.customer.dto.address.AddressResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressClient {

    public AddressResponse createAddress(AddressCreateRequest addressCreateRequest){
        return null;
    }

    public void deleteAddress(Long id){

    }

    public AddressDetailResponse getAddressById(Long id){
        return null;
    }
    public List<AddressResponse> getAddressesByIds(List<Long> ids){return  null;}

}
