package com.lexuancong.address.viewmodel.province;

import com.lexuancong.address.model.Province;
import lombok.Builder;

@Builder
public record ProvinceGetVm(
        Long id,
        String name,
        Long countryId,
        String type
) {
    public static ProvinceGetVm fromModel(Province province) {
        return ProvinceGetVm.builder()
                .id(province.getId())
                .name(province.getName())
                .type(province.getType())
                .countryId(province.getCountry().getId())
                .build();
    }
}
