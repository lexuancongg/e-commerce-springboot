package com.lexuancong.location.viewmodel.country;

import java.util.List;

public record CountryPagingVm(
        List<CountryVm> countryPayload,
        int pageNo,
        int pageSize,
        int totalElements,
        int totalPages,
        boolean isLast
) {
}
