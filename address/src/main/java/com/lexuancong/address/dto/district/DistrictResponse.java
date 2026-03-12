package com.lexuancong.address.dto.district;

import com.lexuancong.address.model.District;
import lombok.Builder;

@Builder
public record DistrictResponse(Long id, String name) {
    public static DistrictResponse fromDistrict(District district) {
        return DistrictResponse.builder()
                .id(district.getId())
                .name(district.getName())
                .build();
    }
}
