package com.lexuancong.inventory.dto.warehouse;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record WarehouseCreateRequest(
        @NotBlank String name,
        @Size(max = 450) String contactName,
        @Size(max = 25) String phoneNumber,
        @Size(max = 450) String specificAddress,
        @NotNull Long districtId,
        @NotNull Long provinceId,
        @NotNull Long countryId
) {
}
