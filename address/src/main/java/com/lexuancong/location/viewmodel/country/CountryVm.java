package com.lexuancong.location.viewmodel.country;

import com.lexuancong.location.model.Country;

public record CountryVm(Long id, String name) {
    public static CountryVm fromModel(Country country) {
        return new CountryVm(country.getId(), country.getName());
    }

}
