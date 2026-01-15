package com.lexuancong.address.service;

import com.lexuancong.address.constants.Constants;
import com.lexuancong.address.model.Address;
import com.lexuancong.address.repository.AddressRepository;
import com.lexuancong.address.repository.CountryRepository;
import com.lexuancong.address.repository.DistrictRepository;
import com.lexuancong.address.repository.ProvinceRepository;
import com.lexuancong.address.dto.address.AddressDetailGetResponse;
import com.lexuancong.address.dto.address.AddressCreateRequest;
import com.lexuancong.address.dto.address.AddressGetResponse;
import com.lexuancong.share.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final CountryRepository countryRepository;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;

    public AddressGetResponse createAddress(AddressCreateRequest addressCreateRequest){
        Address address = addressCreateRequest.toAddressBase();
        this.performSetEntityIfExistsOrThrow(address, addressCreateRequest);
        return AddressGetResponse.fromAddress(addressRepository.save(address));
    }



    public void updateAddress(Long id, AddressCreateRequest addressCreateRequest){
        Address address = addressRepository.findById(id)
                .orElseThrow(() ->  new NotFoundException(Constants.ErrorKey.ADDRESS_NOT_FOUND,id));
        this.performSetEntityIfExistsOrThrow(address, addressCreateRequest);
        address.setContactName(addressCreateRequest.contactName());
        address.setSpecificAddress(addressCreateRequest.specificAddress());
        address.setPhoneNumber(addressCreateRequest.phoneNumber());

        addressRepository.save(address);
    }

    private void performSetEntityIfExistsOrThrow(Address address, AddressCreateRequest addressCreateRequest){
        this.setEntityIfExistsOrThrow(addressCreateRequest.countryId(),countryRepository::findById ,
                Constants.ErrorKey.COUNTRY_NOT_FOUND, address::setCountry);
        this.setEntityIfExistsOrThrow(addressCreateRequest.provinceId(),provinceRepository::findById,
                Constants.ErrorKey.PROVINCE_NOT_FOUND , address::setProvince);
        this.setEntityIfExistsOrThrow(addressCreateRequest.districtId(),districtRepository::findById,
                Constants.ErrorKey.DISTRICT_NOT_FOUND, address::setDistrict);
    }


    public <E> void setEntityIfExistsOrThrow(Long id, Function<Long, Optional<E>> repositoryFindById ,
                                             String errorKey , Consumer<E>  setter){
        Optional<E> optional = repositoryFindById.apply(id);
        if(optional.isEmpty()){
            throw  new NotFoundException(errorKey, id);
        }
        setter.accept(optional.get());

    }



    public AddressDetailGetResponse getAddressById(Long id){
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.ADDRESS_NOT_FOUND,id));
        return AddressDetailGetResponse.fromAddress(address);

    }

    public List<AddressDetailGetResponse> getAddresses(List<Long> ids){
        List<Address> addresses = addressRepository.findAllByIdIn(ids);
        return addresses.stream().map(AddressDetailGetResponse::fromAddress)
                .collect(Collectors.toList());
    }


    public void deleteAddress(Long id){
        Address address = addressRepository
                .findById(id).orElseThrow(() -> new NotFoundException(Constants.ErrorKey.ADDRESS_NOT_FOUND,id));
        addressRepository.delete(address);
    }
}
