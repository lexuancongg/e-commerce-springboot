package com.lexuancong.address.controller;

import com.lexuancong.address.service.CountryService;
import com.lexuancong.address.viewmodel.country.CountryPagingVm;
import com.lexuancong.address.viewmodel.country.CountryPostVm;
import com.lexuancong.address.viewmodel.country.CountryGetVm;
import com.lexuancong.share.constants.Constants;
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


    // checked

    @GetMapping("/management/countries/paging")
    public ResponseEntity<CountryPagingVm> getCountriesPaging(
            @RequestParam(name ="pageIndex" ,defaultValue = Constants.PagingConstants.DEFAULT_PAGE_NUMBER,required = false)
            final int pageIndex,
            @RequestParam(name = "pageSize" ,defaultValue = Constants.PagingConstants.DEFAULT_PAGE_SIZE,required = false)
            final int pageSize
    ){
        return ResponseEntity.ok(countryService.getCountriesPaging(pageIndex,pageSize));
    }


    // checked
    @GetMapping({"/management/countries","/customer/countries"})
    public ResponseEntity<List<CountryGetVm>> getCountries(){
        return ResponseEntity.ok(countryService.getCountries());
    }

    // checked
    @PostMapping("/management/countries")
    public ResponseEntity<CountryGetVm> createCountry(
            @RequestBody @Valid final CountryPostVm countryPostVm,
            final UriComponentsBuilder uriComponentsBuilder
            ){
        CountryGetVm countrySaved = countryService.createCountry(countryPostVm);
        return ResponseEntity.created(
                        uriComponentsBuilder
                                .replacePath("/countries/{id}")
                                .buildAndExpand(countrySaved.id())
                                .toUri())
                .body(countrySaved);
    }

    // checked

    @PutMapping("/management/countries/{id}")
    public ResponseEntity<Void> updateCountry(@PathVariable Long id,
                                              @RequestBody @Valid final CountryPostVm countryPostVm){
        countryService.updateCountry(id,countryPostVm);
        return ResponseEntity.noContent().build();
    }


    // checked
    @DeleteMapping("/management/countries/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id){
        countryService.deleteCountry(id);
        return ResponseEntity.noContent().build();
    }


}
