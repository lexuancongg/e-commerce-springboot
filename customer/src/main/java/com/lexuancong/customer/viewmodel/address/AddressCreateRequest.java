package com.lexuancong.customer.viewmodel.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressCreateRequest(
        @NotBlank String contactName,
        @NotBlank String phoneNumber,
        @NotBlank String specificAddress,
        @NotNull Long districtId,
        @NotNull Long provinceId,
        @NotNull Long countryId
) {
}
