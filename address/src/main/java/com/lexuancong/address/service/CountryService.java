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
        this.validateExitedName(countryPostVm.name(),null);
        return CountryGetVm.fromModel(countryRepository.save(countryPostVm.toModel()));
    }

    private void validateExitedName(String name,Long exitedId){
        if(this.checkExitedName(name,exitedId)){
            throw new DuplicatedException(Constants.ErrorKey.NAME_ALREADY_EXITED, name);
        }
    }
    private boolean checkExitedName(String name,Long exitedId){
        return this.countryRepository.existsByNameIgnoreCaseAndIdNot(name, exitedId);
    }

    public void updateCountry(Long id,CountryPostVm countryPostVm){
       Country country = countryRepository.findById(id)
               .orElseThrow(()-> new NotFoundException(Constants.ErrorKey.COUNTRY_NOT_FOUND, id));
       this.validateExitedName(countryPostVm.name(),id);
       country.setName(countryPostVm.name());
       countryRepository.save(country);

    }
    public void deleteCountry(Long id){
        boolean isExistedCountry = countryRepository.existsById(id);
        if(!isExistedCountry){
            throw new NotFoundException(Constants.ErrorKey.COUNTRY_NOT_FOUND, id);
        }
        // sẽ bị bắn ra ngoại lệ
        countryRepository.deleteById(id);
    }

}
