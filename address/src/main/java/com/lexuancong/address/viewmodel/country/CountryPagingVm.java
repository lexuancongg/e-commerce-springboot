package com.lexuancong.address.viewmodel.country;

import java.util.List;

public record CountryPagingVm(
        List<CountryGetVm> countryPayload,
        int pageIndex,
        int pageSize,
        int totalElements,
        int totalPages,
        boolean isLast
) {
}
