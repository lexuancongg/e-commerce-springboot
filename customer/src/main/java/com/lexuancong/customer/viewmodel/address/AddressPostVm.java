package com.lexuancong.customer.viewmodel.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressPostVm(
        @NotBlank String contactName,
        @NotBlank String phone,
        @NotBlank String addressLine,
        @NotNull Long districtId,
        @NotNull Long provinceId,
        @NotNull Long countryId
) {
}
