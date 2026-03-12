package com.lexuancong.customer.service;

import com.lexuancong.customer.constants.Constants;
import com.lexuancong.customer.model.UserAddress;
import com.lexuancong.customer.repository.UserAddressRepository;
import com.lexuancong.customer.dto.address.AddressDetailResponse;
import com.lexuancong.customer.dto.address.AddressCreateRequest;
import com.lexuancong.customer.dto.address.AddressResponse;
import com.lexuancong.customer.dto.useraddress.UserAddressResponse;
import com.lexuancong.share.exception.NotFoundException;
import com.lexuancong.share.utils.AuthenticationUtils;
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
    private final AddressClient addressClient;

    public UserAddressService(UserAddressRepository userAddressRepository, AddressClient locationService) {
        this.userAddressRepository = userAddressRepository;
        this.addressClient = locationService;
    }


    public UserAddressResponse createUserAddress(AddressCreateRequest addressCreateRequest) {
        String userId =AuthenticationUtils.extractCustomerIdFromJwt();
        List<UserAddress> userAddresses = userAddressRepository.findByUserId(userId);
        boolean isFirstAddress = userAddresses.isEmpty();

        AddressResponse address = addressClient.createAddress(addressCreateRequest);
        UserAddress userAddress = UserAddress.builder()
                .userId(userId)
                .addressId(address.id())
                .isActive(isFirstAddress)
                .build();

        return UserAddressResponse.fromUserAddress(userAddressRepository.save(userAddress),address);
    }

    public AddressDetailResponse getDefaultAddress(){
        String userId = AuthenticationUtils.extractCustomerIdFromJwt();
        UserAddress userAddress = userAddressRepository.findByUserIdAndIsActiveTrue(userId)
                .orElseThrow(()->  new NotFoundException(Constants.ErrorKey.ADDRESS_DEFAULT_NOT_FOUND) );
        return  addressClient.getAddressById(userAddress.getAddressId());

    }

    public void deleteAddress(Long id){
        String userId = AuthenticationUtils.extractCustomerIdFromJwt();
        Optional<UserAddress> optionalUserAddress = userAddressRepository.findOneByUserIdAndAddressId(userId,id);
        if(optionalUserAddress.isEmpty()){
            throw new NotFoundException(Constants.ErrorKey.ADDRESS_NOT_FOUND);
        }
        this.addressClient.deleteAddress(id);
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


    public List<AddressDetailResponse> getUserAddressDetail(){
        String userId = AuthenticationUtils.extractCustomerIdFromJwt();
        List<UserAddress> userAddresses = this.userAddressRepository.findAllByUserId(userId);
        List<Long> addressIds = userAddresses.stream()
                .map(UserAddress::getAddressId)
                .toList();
        List<AddressResponse> addresses = addressClient.getAddressesByIds(addressIds);
        List<AddressDetailResponse> addressDetails = userAddresses.stream()
                .flatMap(userAddress ->
                        addresses.stream()
                                .filter(address -> address.id().equals(userAddress.getAddressId()) )
                                .map(addressFiltered ->
                                        new AddressDetailResponse(
                                                addressFiltered.id(),
                                                addressFiltered.contactName(),
                                                addressFiltered.phoneNumber(),
                                                addressFiltered.specificAddress(),
                                                addressFiltered.districtId(),
                                                addressFiltered.districtName(),
                                                addressFiltered.provinceId(),
                                                addressFiltered.provinceName(),
                                                addressFiltered.countryId(),
                                                addressFiltered.countryName(),
                                                userAddress.isActive()
                                        )
                                )
                ).toList();

        // sxep lai
        // Comparable : muốn so sánh thì class T phải implement nó , như collectTions.sort(list)
        // comparator : dùng để so sánh ngoài như collections.strem.sorted()
        Comparator<AddressDetailResponse> comparator = Comparator.comparing(AddressDetailResponse::isActive).reversed();
        return addressDetails.stream().sorted(comparator).toList();
    }


}
