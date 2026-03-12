package com.lexuancong.customer.dto.customer;

import org.keycloak.representations.idm.UserRepresentation;

public record CustomerResponse(String id, String username, String email, String firstName, String lastName) {
    public static CustomerResponse fromKeycloakUserRes(UserRepresentation userRepresentation){
        return new CustomerResponse(userRepresentation.getId(),userRepresentation.getUsername(),
                userRepresentation.getEmail(),userRepresentation.getFirstName(),userRepresentation.getLastName());
    }
}
