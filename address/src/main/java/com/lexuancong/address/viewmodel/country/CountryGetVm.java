package com.lexuancong.address.viewmodel.country;

import com.lexuancong.address.model.Country;

public record CountryGetVm(Long id, String name) {
    public static CountryGetVm fromModel(Country country) {
        return new CountryGetVm(country.getId(), country.getName());
    }

}
