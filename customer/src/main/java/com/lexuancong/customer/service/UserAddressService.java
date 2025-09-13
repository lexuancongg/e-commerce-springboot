package com.lexuancong.customer.service;

import com.lexuancong.customer.mapper.UserAddressMapper;
import com.lexuancong.customer.model.UserAddress;
import com.lexuancong.customer.repository.UserAddressRepository;
import com.lexuancong.customer.utils.AuthenticationUtils;
import com.lexuancong.customer.viewmodel.address.AddressDetailVm;
import com.lexuancong.customer.viewmodel.address.AddressPostVm;
import com.lexuancong.customer.viewmodel.address.AddressVm;
import com.lexuancong.customer.viewmodel.useraddress.UserAddressVm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class UserAddressService {
    private final UserAddressRepository userAddressRepository;
    private final AddressService addressService;
    private final UserAddressMapper userAddressMapper;

    public UserAddressService(UserAddressRepository userAddressRepository, AddressService locationService, UserAddressMapper userAddressMapper) {
        this.userAddressRepository = userAddressRepository;
        this.addressService = locationService;
        this.userAddressMapper = userAddressMapper;
    }


    public UserAddressVm createUserAddress(AddressPostVm addressPostVm) {
        String userId = AuthenticationUtils.extractCustomerIdFromJwt();
        // check xem co address nào chưa để set isactive = true nếu first
        List<UserAddress> userAddresses = userAddressRepository.findByUserId(userId);
        boolean isFirstAddress = userAddresses.isEmpty();
        // goi api sang service location de them address
        AddressVm addressVmOfAddressSaved = addressService.createAddress(addressPostVm);
        UserAddress userAddress = UserAddress.builder()
                .userId(userId).addressId(addressVmOfAddressSaved.id()).isActive(isFirstAddress)
                .build();
        return userAddressMapper.toVmFromModel(userAddressRepository.save(userAddress),addressVmOfAddressSaved);
    }

    public AddressDetailVm getDefaultAddress(){
        String userId = AuthenticationUtils.extractCustomerIdFromJwt();
        UserAddress userAddress = userAddressRepository.findByUserIdAndIsActiveTrue(userId)
                // ban ra ngoai le here
                .orElseThrow(()-> null );
        return  addressService.getAddressById(userAddress.getAddressId());

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


    // get ds addresss
    public List<AddressDetailVm> getUserAddressDetail(){
        String userId = AuthenticationUtils.extractCustomerIdFromJwt();
        List<UserAddress> userAddresses = this.userAddressRepository.findAllByUserId(userId);
        List<Long> addressIds = userAddresses.stream().map(UserAddress::getAddressId).toList();

        List<AddressVm> addressVms = addressService.getAddressesByIds(addressIds);
        List<AddressDetailVm> addressDetailVms = userAddresses.stream()
                .flatMap(userAddress ->
                        addressVms.stream()
                                .filter(addressVm -> addressVm.id().equals(userAddress.getAddressId()) )
                                .map(addressVmFiltered ->
                                        new AddressDetailVm(
                                                addressVmFiltered.id(),
                                                addressVmFiltered.contactName(),
                                                addressVmFiltered.phoneNumber(),
                                                addressVmFiltered.specificAddress(),
                                                addressVmFiltered.districtId(),
                                                addressVmFiltered.districtName(),
                                                addressVmFiltered.provinceId(),
                                                addressVmFiltered.provinceName(),
                                                addressVmFiltered.countryId(),
                                                addressVmFiltered.countryName(),
                                                userAddress.isActive()
                                        )
                                )
                ).toList();

        // sxep lai
        // Comparable : muốn so sánh thì class T phải implement nó , như collectTions.sort(list)
        // comparator : dùng để so sánh ngoài như collections.strem.sorted()
        Comparator<AddressDetailVm> comparator = Comparator.comparing(AddressDetailVm::isActive).reversed();
        return addressDetailVms.stream().sorted(comparator).toList();
    }


}
