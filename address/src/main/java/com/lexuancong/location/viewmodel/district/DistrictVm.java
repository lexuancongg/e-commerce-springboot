package com.lexuancong.location.viewmodel.district;

import com.lexuancong.location.model.District;
import lombok.Builder;

@Builder
public record DistrictVm(Long id, String name) {
    public static DistrictVm fromModel(District district) {
        return DistrictVm.builder()
                .id(district.getId())
                .name(district.getName())
                .build();
    }
}
