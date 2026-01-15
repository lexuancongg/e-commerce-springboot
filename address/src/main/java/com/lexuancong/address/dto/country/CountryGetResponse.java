package com.lexuancong.address.dto.country;

import com.lexuancong.address.model.Country;

public record CountryGetResponse(Long id, String name) {
    public static CountryGetResponse fromCountry(Country country) {
        return new CountryGetResponse(country.getId(), country.getName());
    }

}
