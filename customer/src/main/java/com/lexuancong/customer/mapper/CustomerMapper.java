package com.lexuancong.customer.mapper;

import com.lexuancong.customer.viewmodel.customer.CustomerVm;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    // kieeur trả về khi gọi api admin keycloak (gọi là dto-viewmodel)
    public CustomerVm toCustomerVmFromUserRepresentation(UserRepresentation userRepresentation){
        return new CustomerVm(userRepresentation.getId(),userRepresentation.getUsername(),
                userRepresentation.getEmail(),userRepresentation.getFirstName(),userRepresentation.getLastName());
    }
}
