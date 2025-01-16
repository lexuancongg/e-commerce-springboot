package com.lexuancong.location.viewmodel.province;

import com.lexuancong.location.model.Province;
import lombok.Builder;

@Builder
public record ProvinceVm(
        Long id,
        String name,
        Long countryId,
        String type
) {
    public static ProvinceVm fromModel(Province province) {
        return ProvinceVm.builder()
                .id(province.getId())
                .name(province.getName())
                .type(province.getType())
                .countryId(province.getCountry().getId())
                .build();
    }
}
