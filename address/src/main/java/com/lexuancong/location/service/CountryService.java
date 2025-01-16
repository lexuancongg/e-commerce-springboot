package com.lexuancong.location.service;

import com.lexuancong.location.model.Country;
import com.lexuancong.location.repository.CountryRepository;
import com.lexuancong.location.viewmodel.country.CountryPagingVm;
import com.lexuancong.location.viewmodel.country.CountryPostVm;
import com.lexuancong.location.viewmodel.country.CountryVm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;

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

        List<CountryVm> countryPayload = countries.stream()
                .map(CountryVm::fromModel)
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

    public List<CountryVm> getCountries(){
        return countryRepository.findAll(Sort.by(Sort.Direction.ASC,"name"))
                .stream()
                .map(CountryVm::fromModel)
                .toList();
    }

    public CountryVm createCountry(CountryPostVm countryPostVm){
        if(countryRepository.existsByNameIgnoreCase(countryPostVm.name())){
            // throw exception
        }
        return CountryVm.fromModel(countryRepository.save(countryPostVm.toModel()));
    }

    public void updateCountry(Long id,CountryPostVm countryPostVm){
       Country country = countryRepository.findById(id).orElseThrow(()->null);
       country.setName(countryPostVm.name());
       countryRepository.save(country);

    }
    public void deleteCountry(Long id){
        boolean isExistCountry = countryRepository.existsById(id);
        if(!isExistCountry){
            // throw exception
        }
        countryRepository.deleteById(id);
    }

}
