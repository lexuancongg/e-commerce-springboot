package com.lexuancong.customer.dto.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CustomerCreateRequest(@NotBlank String username, @NotBlank @Email String email,
                                    @NotBlank String firstName, @NotBlank String lastName
, @NotBlank String password, @NotBlank String role) {
}
