package com.lexuancong.location.viewmodel.country;

import com.lexuancong.location.model.Country;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CountryPostVm(
        @NotBlank @Size(min = 1, max = 450) String name
) {
    public Country toModel(){
        return Country.builder().name(this.name).build();
    }
}
