package com.lexuancong.customer.viewmodel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CustomerPostVm(@NotBlank String username, @NotBlank @Email String email,
                             @NotBlank String firstName, @NotBlank String lastName
,@NotBlank String password,@NotBlank String role) {
}
