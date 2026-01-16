package com.lexuancong.customer.service;

import com.lexuancong.customer.constants.Constants;
import com.lexuancong.customer.model.UserAddress;
import com.lexuancong.customer.repository.UserAddressRepository;
import com.lexuancong.customer.dto.address.AddressDetailGetResponse;
import com.lexuancong.customer.dto.address.AddressCreateRequest;
import com.lexuancong.customer.dto.address.AddressGetResponse;
import com.lexuancong.customer.dto.useraddress.UserAddressGetResponse;
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
    private final AddressService addressService;

    public UserAddressService(UserAddressRepository userAddressRepository, AddressService locationService) {
        this.userAddressRepository = userAddressRepository;
        this.addressService = locationService;
    }


    public UserAddressGetResponse createUserAddress(AddressCreateRequest addressCreateRequest) {
        String userId =AuthenticationUtils.extractCustomerIdFromJwt();
        List<UserAddress> userAddresses = userAddressRepository.findByUserId(userId);
        boolean isFirstAddress = userAddresses.isEmpty();
        // goi api sang service location de them address
        AddressGetResponse addressVmOfAddressSaved = addressService.createAddress(addressCreateRequest);
        UserAddress userAddress = UserAddress.builder()
                .userId(userId)
                .addressId(addressVmOfAddressSaved.id())
                .isActive(isFirstAddress)
                .build();

        return UserAddressGetResponse.fromUserAddress(userAddressRepository.save(userAddress),addressVmOfAddressSaved);
    }

    public AddressDetailGetResponse getDefaultAddress(){
        String userId = AuthenticationUtils.extractCustomerIdFromJwt();
        UserAddress userAddress = userAddressRepository.findByUserIdAndIsActiveTrue(userId)
                .orElseThrow(()->  new NotFoundException(Constants.ErrorKey.ADDRESS_DEFAULT_NOT_FOUND) );
        return  addressService.getAddressById(userAddress.getAddressId());

    }

    public void deleteAddress(Long id){
        String userId = AuthenticationUtils.extractCustomerIdFromJwt();
        Optional<UserAddress> optionalUserAddress = userAddressRepository.findOneByUserIdAndAddressId(userId,id);
        if(optionalUserAddress.isEmpty()){
            throw new NotFoundException(Constants.ErrorKey.ADDRESS_NOT_FOUND);
        }
        this.addressService.deleteAddress(id);
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


    public List<AddressDetailGetResponse> getUserAddressDetail(){
        String userId = AuthenticationUtils.extractCustomerIdFromJwt();
        List<UserAddress> userAddresses = this.userAddressRepository.findAllByUserId(userId);
        List<Long> addressIds = userAddresses.stream()
                .map(UserAddress::getAddressId)
                .toList();
        List<AddressGetResponse> addressGetResponses = addressService.getAddressesByIds(addressIds);
        List<AddressDetailGetResponse> addressDetailGetResponses = userAddresses.stream()
                .flatMap(userAddress ->
                        addressGetResponses.stream()
                                .filter(addressGetResponse -> addressGetResponse.id().equals(userAddress.getAddressId()) )
                                .map(addressGetResponseFiltered ->
                                        new AddressDetailGetResponse(
                                                addressGetResponseFiltered.id(),
                                                addressGetResponseFiltered.contactName(),
                                                addressGetResponseFiltered.phoneNumber(),
                                                addressGetResponseFiltered.specificAddress(),
                                                addressGetResponseFiltered.districtId(),
                                                addressGetResponseFiltered.districtName(),
                                                addressGetResponseFiltered.provinceId(),
                                                addressGetResponseFiltered.provinceName(),
                                                addressGetResponseFiltered.countryId(),
                                                addressGetResponseFiltered.countryName(),
                                                userAddress.isActive()
                                        )
                                )
                ).toList();

        // sxep lai
        // Comparable : muốn so sánh thì class T phải implement nó , như collectTions.sort(list)
        // comparator : dùng để so sánh ngoài như collections.strem.sorted()
        Comparator<AddressDetailGetResponse> comparator = Comparator.comparing(AddressDetailGetResponse::isActive).reversed();
        return addressDetailGetResponses.stream().sorted(comparator).toList();
    }


}
