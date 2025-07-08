package com.lexuancong.address.viewmodel.district;

import com.lexuancong.address.model.District;
import lombok.Builder;

@Builder
public record DistrictGetVm(Long id, String name) {
    public static DistrictGetVm fromModel(District district) {
        return DistrictGetVm.builder()
                .id(district.getId())
                .name(district.getName())
                .build();
    }
}
