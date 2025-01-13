package com.lexuancong.customer.service;

import com.lexuancong.customer.mapper.UserAddressMapper;
import com.lexuancong.customer.model.UserAddress;
import com.lexuancong.customer.repository.UserAddressRepository;
import com.lexuancong.customer.utils.AuthenticationUtils;
import com.lexuancong.customer.viewmodel.address.AddressDetailVm;
import com.lexuancong.customer.viewmodel.address.AddressPostVm;
import com.lexuancong.customer.viewmodel.address.AddressVm;
import com.lexuancong.customer.viewmodel.useraddress.UserAddressVm;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class UserAddressService {
    private final UserAddressRepository userAddressRepository;
    private final LocationService locationService;
    private final UserAddressMapper userAddressMapper;

    public UserAddressService(UserAddressRepository userAddressRepository, LocationService locationService, UserAddressMapper userAddressMapper) {
        this.userAddressRepository = userAddressRepository;
        this.locationService = locationService;
        this.userAddressMapper = userAddressMapper;
    }


    public UserAddressVm createAddress(AddressPostVm addressPostVm) {
        String userId = AuthenticationUtils.extractCustomerIdFromJwt();
        // check xem co address nào chưa để set isactive = true nếu first
        List<UserAddress> userAddresses = userAddressRepository.findByUserId(userId);
        boolean isFirstAddress = userAddresses.isEmpty();
        // goi api sang service location de them address
        AddressVm addressVmOfSavedAddress = locationService.createAddress(addressPostVm);
        UserAddress userAddress = UserAddress.builder()
                .userId(userId).addressId(addressVmOfSavedAddress.id()).isActive(isFirstAddress)
                .build();
        return userAddressMapper.toVmFromModel(userAddressRepository.save(userAddress),addressVmOfSavedAddress);
    }

    public AddressDetailVm getDefaultAddress(){
        String userId = AuthenticationUtils.extractCustomerIdFromJwt();
        UserAddress userAddress = userAddressRepository.findByUserIdAndIsActiveTrue(userId)
                // ban ra ngoai le here
                .orElseThrow(()-> null );
        return  locationService.getAddressById(userAddress.getAddressId());

    }

    public void deleteAddress(Long id){
        String userId = AuthenticationUtils.extractCustomerIdFromJwt();
        Optional<UserAddress> optionalUserAddress = userAddressRepository.findOneByUserIdAndAddressId(userId,id);
        if(optionalUserAddress.isEmpty()){
            // throw exception
        }
        userAddressRepository.delete(optionalUserAddress.get());

    }
    public void chooseDefaultAddress(Long id){
        String userId = AuthenticationUtils.extractCustomerIdFromJwt();
        List<UserAddress> userAddresses = userAddressRepository.findAllByUserId(userId);
        for(UserAddress userAddress : userAddresses){
            userAddress.setActive(Objects.equals(userAddress.getAddressId(),id));
        }
        userAddressRepository.saveAll(userAddresses);

    }


}
