package com.lexuancong.customer.service;

import com.lexuancong.customer.mapper.UserAddressMapper;
import com.lexuancong.customer.model.CustomerAddress;
import com.lexuancong.customer.repository.CustomerAddressRepository;
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
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerAddressService {
    private final CustomerAddressRepository customerAddressRepository;
    private final AddressService addressService;
    private final UserAddressMapper userAddressMapper;

    public CustomerAddressService(CustomerAddressRepository customerAddressRepository, AddressService locationService, UserAddressMapper userAddressMapper) {
        this.customerAddressRepository = customerAddressRepository;
        this.addressService = locationService;
        this.userAddressMapper = userAddressMapper;
    }


    public UserAddressVm createAddress(AddressPostVm addressPostVm) {
        String userId = AuthenticationUtils.extractCustomerIdFromJwt();
        // check xem co address nào chưa để set isactive = true nếu first
        List<CustomerAddress> customerAddresses = customerAddressRepository.findByUserId(userId);
        boolean isFirstAddress = customerAddresses.isEmpty();
        // goi api sang service location de them address
        AddressVm addressVmOfSavedAddress = addressService.createAddress(addressPostVm);
        CustomerAddress customerAddress = CustomerAddress.builder()
                .userId(userId).addressId(addressVmOfSavedAddress.id()).isActive(isFirstAddress)
                .build();
        return userAddressMapper.toVmFromModel(customerAddressRepository.save(customerAddress),addressVmOfSavedAddress);
    }

    public AddressDetailVm getDefaultAddress(){
        String userId = AuthenticationUtils.extractCustomerIdFromJwt();
        CustomerAddress customerAddress = customerAddressRepository.findByUserIdAndIsActiveTrue(userId)
                // ban ra ngoai le here
                .orElseThrow(()-> null );
        return  addressService.getAddressById(customerAddress.getAddressId());

    }

    public void deleteAddress(Long id){
        String userId = AuthenticationUtils.extractCustomerIdFromJwt();
        Optional<CustomerAddress> optionalUserAddress = customerAddressRepository.findOneByUserIdAndAddressId(userId,id);
        if(optionalUserAddress.isEmpty()){
            // throw exception
        }
        customerAddressRepository.delete(optionalUserAddress.get());

    }
    public void chooseDefaultAddress(Long id){
        String userId = AuthenticationUtils.extractCustomerIdFromJwt();
        List<CustomerAddress> customerAddresses = customerAddressRepository.findAllByUserId(userId);
        for(CustomerAddress customerAddress : customerAddresses){
            customerAddress.setActive(Objects.equals(customerAddress.getAddressId(),id));
        }
        customerAddressRepository.saveAll(customerAddresses);

    }


    // get ds addresss
    public List<AddressDetailVm> getDetailAddresses(){
        String userId = AuthenticationUtils.extractCustomerIdFromJwt();
        List<CustomerAddress> customerAddresses = this.customerAddressRepository.findAllByUserId(userId);
        List<Long> addressIds = customerAddresses.stream().map(CustomerAddress::getAddressId).toList();

        List<AddressVm> addressVms = addressService.getAddressesByIds(addressIds);
        List<AddressDetailVm> addressDetailVms = customerAddresses.stream()
                .flatMap(customerAddress ->
                        addressVms.stream()
                                .filter(addressVm -> addressVm.id().equals(customerAddress.getAddressId()) )
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
                                                customerAddress.isActive()
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
