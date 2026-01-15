package com.lexuancong.customer.viewmodel.customer;

public record CustomerProfileUpdateRequest(
        String firstName,
        String lastName,
        String email
) {
}
