package com.lexuancong.inventory.viewmodel.address;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddressPostVm(
        @Size(max = 450) String contactName,
        @Size(max = 25) String phoneNumber,
        @Size(max = 450) String specificAddress,
        @NotNull Long districtId,
        @NotNull Long provinceId,
        @NotNull Long countryId
){

}
