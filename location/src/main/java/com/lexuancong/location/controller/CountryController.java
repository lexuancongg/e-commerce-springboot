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
    @GetMapping("/backoffice/countries/paging")
    public ResponseEntity<CountryPagingVm> getCountriesPaging(
            @RequestParam(name ="pageIndex" ,defaultValue = Constants.PagingConstants.DEFAULT_PAGE_NUMBER,required = false)
            final int pageIndex,
            @RequestParam(name = "pageSize" ,defaultValue = Constants.PagingConstants.DEFAULT_PAGE_SIZE,required = false)
            final int pageSize
    ){
        return ResponseEntity.ok(countryService.getCountriesPaging(pageIndex,pageSize));
    }

    @GetMapping("/backoffice/countries")
    public ResponseEntity<List<CountryVm>> getCountries(){
        return ResponseEntity.ok(countryService.getCountries());
    }

    @PostMapping("/backoffice/country")
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


}
