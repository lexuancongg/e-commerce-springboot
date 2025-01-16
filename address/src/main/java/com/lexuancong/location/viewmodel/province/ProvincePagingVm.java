package com.lexuancong.location.viewmodel.province;

import lombok.Builder;

import java.util.List;
@Builder
public record ProvincePagingVm(
        List<ProvinceVm> provincePayload,
        int pageIndex,
        int pageSize,
        int totalElements,
        int totalPages,
        boolean isLast

) {
}
