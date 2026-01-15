package com.lexuancong.address.service;

import com.lexuancong.address.constants.Constants;
import com.lexuancong.address.model.Country;
import com.lexuancong.address.repository.CountryRepository;
import com.lexuancong.address.dto.country.CountryPagingGetResponse;
import com.lexuancong.address.dto.country.CountryCreateRequest;
import com.lexuancong.address.dto.country.CountryGetResponse;
import com.lexuancong.share.exception.DuplicatedException;
import com.lexuancong.share.exception.NotFoundException;
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


    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public CountryPagingGetResponse getCountriesPaging(final int pageIndex, final int pageSize){
        final Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(Sort.Direction.ASC,"name"));
        final Page<Country> countryPage = countryRepository.findAll(pageable);
        List<Country> countries = countryPage.getContent();

        List<CountryGetResponse> countryPayload = countries.stream()
                .map(CountryGetResponse::fromCountry)
                .toList();
        return new CountryPagingGetResponse(
                countryPayload,
                countryPage.getNumber(),
                countryPage.getSize(),
                (int) countryPage.getTotalElements(),
                countryPage.getTotalPages(),
                countryPage.isLast()
        );

    }



    public List<CountryGetResponse> getCountries(){
        return countryRepository.findAll(Sort.by(Sort.Direction.ASC,"name"))
                .stream()
                .map(CountryGetResponse::fromCountry)
                .toList();
    }

    public CountryGetResponse createCountry(CountryCreateRequest countryCreateRequest){
        this.validateExitedName(countryCreateRequest.name(),null);
        return CountryGetResponse.fromCountry(countryRepository.save(countryCreateRequest.toCountry()));
    }

    private void validateExitedName(String name,Long exitedId){
        if(this.checkExitedName(name,exitedId)){
            throw new DuplicatedException(Constants.ErrorKey.NAME_ALREADY_EXITED, name);
        }
    }
    private boolean checkExitedName(String name,Long exitedId){
        return this.countryRepository.existsByNameIgnoreCaseAndIdNot(name, exitedId);
    }

    public void updateCountry(Long id, CountryCreateRequest countryCreateRequest){
       Country country = countryRepository.findById(id)
               .orElseThrow(()-> new NotFoundException(Constants.ErrorKey.COUNTRY_NOT_FOUND, id));
       this.validateExitedName(countryCreateRequest.name(),id);
       country.setName(countryCreateRequest.name());
       countryRepository.save(country);

    }
    public void deleteCountry(Long id){
        boolean isExistedCountry = countryRepository.existsById(id);
        if(!isExistedCountry){
            throw new NotFoundException(Constants.ErrorKey.COUNTRY_NOT_FOUND, id);
        }
        countryRepository.deleteById(id);
    }

}
