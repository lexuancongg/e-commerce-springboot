package com.lexuancong.customer.dto.customer;

import org.keycloak.representations.idm.UserRepresentation;

public record CustomerGetResponse(String id, String username, String email, String firstName, String lastName) {
    public static CustomerGetResponse fromKeycloakUserRes(UserRepresentation userRepresentation){
        return new CustomerGetResponse(userRepresentation.getId(),userRepresentation.getUsername(),
                userRepresentation.getEmail(),userRepresentation.getFirstName(),userRepresentation.getLastName());
    }
}
