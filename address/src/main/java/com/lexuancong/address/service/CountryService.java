package com.lexuancong.address.service;

import com.lexuancong.address.constants.Constants;
import com.lexuancong.address.model.Country;
import com.lexuancong.address.repository.AddressRepository;
import com.lexuancong.address.repository.CountryRepository;
import com.lexuancong.address.dto.country.CountryCreateRequest;
import com.lexuancong.address.dto.country.CountryResponse;
import com.lexuancong.address.repository.ProvinceRepository;
import com.lexuancong.share.dto.paging.PagingResponse;
import com.lexuancong.share.exception.DuplicatedException;
import com.lexuancong.share.exception.NotFoundException;
import com.lexuancong.share.exception.ResourceInUseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional

public class CountryService {
    private final CountryRepository countryRepository;
    private final ProvinceRepository provinceRepository;
    private final AddressRepository addressRepository;


    public CountryService(CountryRepository countryRepository, ProvinceRepository provinceRepository, AddressRepository addressRepository) {
        this.countryRepository = countryRepository;
        this.provinceRepository = provinceRepository;
        this.addressRepository = addressRepository;
    }

    public PagingResponse<CountryResponse> getCountriesPaging(final int pageIndex, final int pageSize){
        final Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(Sort.Direction.ASC,"name"));
        final Page<Country> countryPage = countryRepository.findAll(pageable);
        List<Country> countries = countryPage.getContent();

        List<CountryResponse> countryPayload = countries.stream()
                .map(CountryResponse::fromCountry)
                .toList();
        return PagingResponse.<CountryResponse>builder()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .totalElements(countryPage.getTotalElements())
                .totalPages(countryPage.getTotalPages())
                .payload(countryPayload)
                .last(countryPage.isLast())
                .build();

    }



    public List<CountryResponse> getCountries(){
        return countryRepository.findAll(Sort.by(Sort.Direction.ASC,"name"))
                .stream()
                .map(CountryResponse::fromCountry)
                .toList();
    }

    public CountryResponse createCountry(CountryCreateRequest countryCreateRequest){
        this.validateNameExists(countryCreateRequest.name(),null);
        return CountryResponse.fromCountry(
                countryRepository.save(countryCreateRequest.toCountry())
        );
    }




    private void validateNameExists(String name, Long exitedId){
        if(this.checkNameExists(name,exitedId)){
            throw new DuplicatedException(Constants.ErrorKey.NAME_ALREADY_EXITED, name);
        }
    }
    private boolean checkNameExists(String name, Long exitedId){
        return this.countryRepository.existsByNameIgnoreCaseAndIdNot(name, exitedId);
    }

    public void updateCountry(Long id, CountryCreateRequest countryCreateRequest){
       Country country = countryRepository.findById(id)
               .orElseThrow(()-> new NotFoundException(Constants.ErrorKey.COUNTRY_NOT_FOUND, id));
       this.validateNameExists(countryCreateRequest.name(),id);
       country.setName(countryCreateRequest.name());
       countryRepository.save(country);

    }


    public void deleteCountry(Long id){
        boolean countryExists = countryRepository.existsById(id);
        if(!countryExists){
            throw new NotFoundException(Constants.ErrorKey.COUNTRY_NOT_FOUND, id);
        }
        if(provinceRepository.existsByCountry_Id(id)){
            throw new ResourceInUseException(Constants.ErrorKey.COUNTRY_IS_USED_BY_PROVINCE);
        }
        if(addressRepository.existsByCountry_Id(id)){
            throw new ResourceInUseException(Constants.ErrorKey.COUNTRY_IS_USED_BY_ADDRESS);
        }
        countryRepository.deleteById(id);
    }

}
