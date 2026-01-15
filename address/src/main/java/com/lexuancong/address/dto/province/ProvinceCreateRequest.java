package com.lexuancong.address.dto.province;

import com.lexuancong.address.model.Country;
import com.lexuancong.address.model.Province;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ProvinceCreateRequest(
        @NotBlank @Size(min = 1,max = 100) String name,
        String type,
        @NotNull Long countryId
) {
    public Province toModel(Country country){
        return Province.builder()
                .name(name).type(type).country(country)
                .build();
    }
}
