package com.lexuancong.address.viewmodel.address;

import com.lexuancong.address.model.Address;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddressPostVm(
        @Size(max = 450) String contactName,
        @Size(max = 25) String phoneNumber,
        @Size(max = 450) String specificAddress,
        @NotNull Long districtId,
        @NotNull Long provinceId,
        @NotNull Long countryId
) {
    public Address toModel(){
        return Address.builder()
                .contactName(this.contactName)
                .phoneNumber(this.phoneNumber)
                .specificAddress(this.specificAddress)
                .build();
    }
}
