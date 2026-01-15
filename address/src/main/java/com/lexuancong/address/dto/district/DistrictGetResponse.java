package com.lexuancong.address.dto.district;

import com.lexuancong.address.model.District;
import lombok.Builder;

@Builder
public record DistrictGetResponse(Long id, String name) {
    public static DistrictGetResponse fromDistrict(District district) {
        return DistrictGetResponse.builder()
                .id(district.getId())
                .name(district.getName())
                .build();
    }
}
