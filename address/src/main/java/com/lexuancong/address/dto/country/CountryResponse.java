package com.lexuancong.address.dto.country;

import com.lexuancong.address.model.Country;

public record CountryResponse(Long id, String name) {
    public static CountryResponse fromCountry(Country country) {
        return new CountryResponse(country.getId(), country.getName());
    }

}
