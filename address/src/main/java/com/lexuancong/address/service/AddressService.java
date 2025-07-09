package com.lexuancong.address.service;

import com.lexuancong.address.constants.Constants;
import com.lexuancong.address.model.Address;
import com.lexuancong.address.model.Country;
import com.lexuancong.address.repository.AddressRepository;
import com.lexuancong.address.repository.CountryRepository;
import com.lexuancong.address.repository.DistrictRepository;
import com.lexuancong.address.repository.ProvinceRepository;
import com.lexuancong.address.viewmodel.address.AddressDetailVm;
import com.lexuancong.address.viewmodel.address.AddressPostVm;
import com.lexuancong.address.viewmodel.address.AddressGetVm;
import com.lexuancong.share.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final CountryRepository countryRepository;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;

    public AddressGetVm createAddress(AddressPostVm addressPostVm){
        Address address = addressPostVm.toModel();
        Country country = countryRepository.findById(addressPostVm.countryId())
                .orElseThrow(() ->
                        new NotFoundException(Constants.ErrorKey.Country.COUNTRY_NOT_FOUND, addressPostVm.countryId()));
        address.setCountry(country);
        provinceRepository.findById(addressPostVm.provinceId())
                .ifPresent(address::setProvince);

        districtRepository.findById(addressPostVm.districtId()).ifPresent(address::setDistrict);
        return AddressGetVm.fromModel(addressRepository.save(address));
    }

    public void updateAddress(Long id, AddressPostVm addressPostVm){
        // throw exception
        Address address = addressRepository.findById(id)
                .orElseThrow(() ->  new NotFoundException(Constants.ErrorKey.Address.ADDRESS_NOT_FOUND,id));
        address.setContactName(addressPostVm.contactName());
        address.setSpecificAddress(addressPostVm.specificAddress());
        address.setPhoneNumber(addressPostVm.phoneNumber());

        countryRepository.findById(addressPostVm.countryId()).ifPresent(address::setCountry);
        provinceRepository.findById(addressPostVm.provinceId()).ifPresent(address::setProvince);
        districtRepository.findById(addressPostVm.districtId()).ifPresent(address::setDistrict);
        addressRepository.save(address);
    }

    public AddressDetailVm getAddressById(Long id){
        // throw exception
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.Address.ADDRESS_NOT_FOUND,id));
        return AddressDetailVm.fromModel(address);

    }

    public List<AddressDetailVm> getAddresses(List<Long> ids){
        List<Address> addresses = addressRepository.findAllByIdIn(ids);
        return addresses.stream().map(AddressDetailVm::fromModel).collect(Collectors.toList());

    }
    public void deleteAddress(Long id){
        Address address = addressRepository
                .findById(id).orElseThrow(() -> new NotFoundException(Constants.ErrorKey.Address.ADDRESS_NOT_FOUND,id));
        addressRepository.delete(address);
    }
}
