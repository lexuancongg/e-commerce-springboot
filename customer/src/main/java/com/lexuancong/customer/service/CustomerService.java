package com.lexuancong.customer.service;

import com.lexuancong.customer.config.KeycloakPropsConfig;
import com.lexuancong.customer.mapper.CustomerMapper;
import com.lexuancong.customer.utils.AuthenticationUtils;
import com.lexuancong.customer.viewmodel.customer.CustomerPostVm;
import com.lexuancong.customer.viewmodel.customer.CustomerVm;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CustomerService {
    private final Keycloak keycloak;
    private final KeycloakPropsConfig keycloakPropsConfig;
    private final CustomerMapper customerMapper;

    public CustomerService(Keycloak keycloak, KeycloakPropsConfig keycloakPropsConfig, CustomerMapper customerMapper) {
        this.keycloak = keycloak;
        this.keycloakPropsConfig = keycloakPropsConfig;
        this.customerMapper = customerMapper;
    }

    public CustomerVm getCustomerProfile(){
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        try {
            UserRepresentation userFromKeycloak = keycloak.realm(keycloakPropsConfig.getRealm()).users().get(customerId).toRepresentation();
            return customerMapper.toCustomerVmFromUserRepresentation(userFromKeycloak);
            // khi không đủ quyền truy cập
        }catch (ForbiddenException forbiddenException){
            // throw exception
        }
    }

    public CustomerVm createCustomer(CustomerPostVm customerPostVm){
        RealmResource realmResource = keycloak.realm(keycloakPropsConfig.getRealm());
        if(this.checkUsernameExistInRealm(realmResource,customerPostVm.username())){
            // bắn ngoại lệ
        }
        if(this.checkEmailExistInRealm(realmResource,customerPostVm.email())){
            // băn ra ngoại le
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

        // sau này xử lý role trường hợp admin ta tài khoản cho nhân viên ở đây
        return customerMapper.toCustomerVmFromUserRepresentation(user);

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
}
