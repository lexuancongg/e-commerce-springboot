package com.lexuancong.customer.dto.customer;

public record CustomerProfileUpdateRequest(
        String firstName,
        String lastName,
        String email
) {
}
