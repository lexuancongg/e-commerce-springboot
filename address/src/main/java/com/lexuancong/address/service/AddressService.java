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

import javax.swing.text.html.Option;
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

    public AddressGetVm createAddress(AddressPostVm addressPostVm){
        Address address = addressPostVm.toModel();
        this.performSetEntityIfExistsOrThrow(address,addressPostVm);
        return AddressGetVm.fromModel(addressRepository.save(address));
    }

    public void updateAddress(Long id, AddressPostVm addressPostVm){
        // throw exception
        Address address = addressRepository.findById(id)
                .orElseThrow(() ->  new NotFoundException(Constants.ErrorKey.ADDRESS_NOT_FOUND,id));
        this.performSetEntityIfExistsOrThrow(address,addressPostVm);
        address.setContactName(addressPostVm.contactName());
        address.setSpecificAddress(addressPostVm.specificAddress());
        address.setPhoneNumber(addressPostVm.phoneNumber());


        addressRepository.save(address);
    }

    private void performSetEntityIfExistsOrThrow(Address address, AddressPostVm addressPostVm ){
        this.setEntityIfExistsOrThrow(addressPostVm.countryId(),countryRepository::findById ,
                Constants.ErrorKey.COUNTRY_NOT_FOUND, address::setCountry);
        this.setEntityIfExistsOrThrow(addressPostVm.provinceId(),provinceRepository::findById,
                Constants.ErrorKey.PROVINCE_NOT_FOUND , address::setProvince);
        this.setEntityIfExistsOrThrow(addressPostVm.districtId(),districtRepository::findById,
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



    public AddressDetailVm getAddressById(Long id){
        // throw exception
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.ADDRESS_NOT_FOUND,id));
        return AddressDetailVm.fromModel(address);

    }

    public List<AddressDetailVm> getAddresses(List<Long> ids){
        List<Address> addresses = addressRepository.findAllByIdIn(ids);
        return addresses.stream().map(AddressDetailVm::fromModel).collect(Collectors.toList());
    }
    public void deleteAddress(Long id){
        Address address = addressRepository
                .findById(id).orElseThrow(() -> new NotFoundException(Constants.ErrorKey.ADDRESS_NOT_FOUND,id));
        addressRepository.delete(address);
    }
}
