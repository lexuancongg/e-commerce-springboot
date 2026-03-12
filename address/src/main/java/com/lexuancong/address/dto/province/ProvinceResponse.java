package com.lexuancong.address.dto.province;

import com.lexuancong.address.model.Province;
import lombok.Builder;

@Builder
public record ProvinceResponse(
        Long id,
        String name,
        Long countryId,
        String type
) {
    public static ProvinceResponse fromProvince(Province province) {
        return ProvinceResponse.builder()
                .id(province.getId())
                .name(province.getName())
                .type(province.getType())
                .countryId(province.getCountry().getId())
                .build();
    }
}
