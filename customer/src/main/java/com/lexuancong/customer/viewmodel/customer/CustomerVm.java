package com.lexuancong.customer.viewmodel.customer;

import org.keycloak.representations.idm.UserRepresentation;

public record CustomerVm(String id, String username, String email, String firstName, String lastName) {
    public static CustomerVm fromKeycloakUserRes(UserRepresentation userRepresentation){
        return new CustomerVm(userRepresentation.getId(),userRepresentation.getUsername(),
                userRepresentation.getEmail(),userRepresentation.getFirstName(),userRepresentation.getLastName());
    }
}
