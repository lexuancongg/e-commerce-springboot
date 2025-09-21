package com.lexuancong.customer.service;

import com.lexuancong.customer.config.KeycloakPropsConfig;
import com.lexuancong.customer.constants.Constants;
import com.lexuancong.customer.viewmodel.customer.CustomerPostVm;
import com.lexuancong.customer.viewmodel.customer.CustomerProfilePutVm;
import com.lexuancong.customer.viewmodel.customer.CustomerVm;
import com.lexuancong.share.exception.AccessDeniedException;
import com.lexuancong.share.exception.DuplicatedException;
import com.lexuancong.share.utils.AuthenticationUtils;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// keycloak lưu thông tin người dùng mặc điịnh có các thuộc tính như firstname, lastname, email... , nếu thêm thuộc tính khác thì thêm vào phần attribute
@Service
public class CustomerService {
    private final Keycloak keycloak;
    private final KeycloakPropsConfig keycloakPropsConfig;

    public CustomerService(Keycloak keycloak, KeycloakPropsConfig keycloakPropsConfig) {
        this.keycloak = keycloak;
        this.keycloakPropsConfig = keycloakPropsConfig;
    }

    public CustomerVm getCustomerProfile(){
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        try {
            UserRepresentation userFromKeycloak = keycloak.realm(keycloakPropsConfig.getRealm())
                    .users()
                    .get(customerId)
                    .toRepresentation();
            return CustomerVm.fromKeycloakUserRes(userFromKeycloak);
        }catch (ForbiddenException forbiddenException){
            throw new AccessDeniedException(Constants.ErrorKey.ACCESS_DENIED_KEYCLOAK);
        }

    }



    public CustomerVm createCustomer(CustomerPostVm customerPostVm){
        RealmResource realmResource = keycloak.realm(keycloakPropsConfig.getRealm());
        if(this.checkUsernameExistInRealm(realmResource,customerPostVm.username())){
             throw  new DuplicatedException(Constants.ErrorKey.USERNAME_ALREADY_EXISTS);
        }
        if(this.checkEmailExistInRealm(realmResource,customerPostVm.email())){
            throw  new DuplicatedException(Constants.ErrorKey.EMAIL_ALREADY_EXISTS);
        }

        UserRepresentation user = new UserRepresentation();
        user.setUsername(customerPostVm.username());
        user.setEmail(customerPostVm.email());
        user.setFirstName(customerPostVm.firstName());
        user.setLastName(customerPostVm.lastName());

        // thông tin bảo mật
        CredentialRepresentation credential = this.createPasswordCredentials(customerPostVm.password());
        List<CredentialRepresentation> credentials = Collections.singletonList(credential);
        user.setCredentials(credentials);
        user.setEnabled(true);
        Response  responseCreateUser = realmResource.users().create(user);
        String userId = CreatedResponseUtil.getCreatedId(responseCreateUser);

        UserResource userResource = realmResource.users().get(userId);

        RoleRepresentation role = realmResource.roles().get(customerPostVm.role()).toRepresentation();

        List<RoleRepresentation> roles = Collections.singletonList(role);

        userResource.roles().realmLevel().add(roles);

        return  CustomerVm.fromKeycloakUserRes(user);
    }
    private boolean checkUsernameExistInRealm(RealmResource realmResource,String username){
        // phân biệt hoa thường khong
        List<UserRepresentation> users = realmResource.users().search(username,true);
        return !users.isEmpty();

    }
    private boolean checkEmailExistInRealm(RealmResource realmResource,String email){
        List<UserRepresentation> users = realmResource.users().searchByEmail(email,true);
        return !users.isEmpty();
    }

    private CredentialRepresentation createPasswordCredentials(String password){
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        passwordCredentials.setTemporary(false);
        return passwordCredentials;

    }

    public void updateCustomerProfile(CustomerProfilePutVm customerProfilePutVm){
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        try{
            RealmResource realmResource = keycloak.realm(keycloakPropsConfig.getRealm());
            UserResource userResource = realmResource.users().get(customerId);
            UserRepresentation userRepresentation = userResource.toRepresentation();
            if(userRepresentation!=null){
                userRepresentation.setFirstName(customerProfilePutVm.firstName());
                userRepresentation.setLastName(customerProfilePutVm.lastName());
                userRepresentation.setEmail(customerProfilePutVm.email());
                userResource.update(userRepresentation);
            }

        }catch (ForbiddenException forbiddenException){
            throw new AccessDeniedException(Constants.ErrorKey.ACCESS_DENIED_KEYCLOAK);
        }

    }

    public List<CustomerVm> getCustomers(){
        try {
            return this.keycloak.realm(keycloakPropsConfig.getRealm()).users()
                    .search(null,0,100)
                    .stream()
                    .filter(UserRepresentation::isEmailVerified)
                    .map(CustomerVm::fromKeycloakUserRes)
                    .toList();
        }catch (ForbiddenException forbiddenException){
            throw  new AccessDeniedException(Constants.ErrorKey.ACCESS_DENIED_KEYCLOAK);
        }
    }
}
