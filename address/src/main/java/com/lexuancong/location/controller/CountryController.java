package com.lexuancong.location.controller;

import com.lexuancong.location.model.Country;
import com.lexuancong.location.service.CountryService;
import com.lexuancong.location.utils.Constants;
import com.lexuancong.location.viewmodel.country.CountryPagingVm;
import com.lexuancong.location.viewmodel.country.CountryPostVm;
import com.lexuancong.location.viewmodel.country.CountryVm;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
public class CountryController {
    private final CountryService countryService;
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }
    @GetMapping("/management/countries/paging")
    public ResponseEntity<CountryPagingVm> getCountriesPaging(
            @RequestParam(name ="pageIndex" ,defaultValue = Constants.PagingConstants.DEFAULT_PAGE_NUMBER,required = false)
            final int pageIndex,
            @RequestParam(name = "pageSize" ,defaultValue = Constants.PagingConstants.DEFAULT_PAGE_SIZE,required = false)
            final int pageSize
    ){
        return ResponseEntity.ok(countryService.getCountriesPaging(pageIndex,pageSize));
    }

    @GetMapping({"/management/countries","/storefront/countries"})
    public ResponseEntity<List<CountryVm>> getCountries(){
        return ResponseEntity.ok(countryService.getCountries());
    }

    @PostMapping("/management/country")
    public ResponseEntity<CountryVm> createCountry(
            @RequestBody @Valid final CountryPostVm countryPostVm,
            final UriComponentsBuilder uriComponentsBuilder
            ){
        CountryVm countrySaved = countryService.createCountry(countryPostVm);
        return ResponseEntity.created(
                        uriComponentsBuilder
                                .replacePath("/country/{id}")
                                .buildAndExpand(countrySaved.id())
                                .toUri())
                .body(countrySaved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCountry(@PathVariable Long id,
                                              @RequestBody @Valid final CountryPostVm countryPostVm){
        countryService.updateCountry(id,countryPostVm);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/management/country/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id){
        countryService.deleteCountry(id);
        return ResponseEntity.noContent().build();
    }


}
