package com.lexuancong.address.service;

import com.lexuancong.address.constants.Constants;
import com.lexuancong.address.model.Country;
import com.lexuancong.address.repository.CountryRepository;
import com.lexuancong.address.viewmodel.country.CountryPagingVm;
import com.lexuancong.address.viewmodel.country.CountryPostVm;
import com.lexuancong.address.viewmodel.country.CountryGetVm;
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

    public CountryPagingVm getCountriesPaging(final int pageIndex,final int pageSize){
        final Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(Sort.Direction.ASC,"name"));
        final Page<Country> countryPage = countryRepository.findAll(pageable);
        List<Country> countries = countryPage.getContent();

        List<CountryGetVm> countryPayload = countries.stream()
                .map(CountryGetVm::fromModel)
                .toList();
        return new CountryPagingVm(
                countryPayload,
                countryPage.getNumber(),
                countryPage.getSize(),
                (int) countryPage.getTotalElements(),
                countryPage.getTotalPages(),
                countryPage.isLast()
        );

    }

    public List<CountryGetVm> getCountries(){
        return countryRepository.findAll(Sort.by(Sort.Direction.ASC,"name"))
                .stream()
                .map(CountryGetVm::fromModel)
                .toList();
    }

    public CountryGetVm createCountry(CountryPostVm countryPostVm){
        if(countryRepository.existsByNameIgnoreCase(countryPostVm.name())){
            throw new DuplicatedException(Constants.ErrorKey.NAME_ALREADY_EXITED);
        }
        return CountryGetVm.fromModel(countryRepository.save(countryPostVm.toModel()));
    }

    public void updateCountry(Long id,CountryPostVm countryPostVm){
       Country country = countryRepository.findById(id)
               .orElseThrow(()-> new NotFoundException(Constants.ErrorKey.Country.COUNTRY_NOT_FOUND, id));
       country.setName(countryPostVm.name());
       if(this.countryRepository.existsByNameIgnoreCaseAndIdNot(countryPostVm.name(),id)){
           // throw exception
       }
       countryRepository.save(country);

    }
    public void deleteCountry(Long id){
        boolean isExistedCountry = countryRepository.existsById(id);
        if(!isExistedCountry){
            throw new NotFoundException(Constants.ErrorKey.Country.COUNTRY_NOT_FOUND, id);
        }
        countryRepository.deleteById(id);
    }

}
