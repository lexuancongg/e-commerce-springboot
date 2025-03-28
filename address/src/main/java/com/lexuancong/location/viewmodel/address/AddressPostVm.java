package com.lexuancong.location.viewmodel.address;

import com.lexuancong.location.model.Address;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddressPostVm(
        @Size(max = 450) String contactName,
        @Size(max = 25) String phone,
        @Size(max = 450) String addressLine,
        @NotNull Long districtId,
        @NotNull Long provinceId,
        @NotNull Long countryId
) {
    public Address toModel(){
        return Address.builder()
                .contactName(this.contactName)
                .phone(this.phone)
                .specificAddress(this.addressLine)
                .build();
    }
}
