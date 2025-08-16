package com.lexuancong.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
// config theo Reactive application (webFlux)
public class SecurityConfig {
    private final ReactiveClientRegistrationRepository clientRegistrationRepository;
    private  final String REALM_ACCESS_CLAIM = "realm_access";
    private  final String ROLES_CLAIM = "roles";

    // bean sẽ được inject từ file cấu hinhf
    public SecurityConfig(ReactiveClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return  http
                .authorizeExchange(auth -> auth
                        .pathMatchers("/").authenticated()
                        .anyExchange().permitAll())
                // dùng oauth2 mặc định , spring tự động cấu hình url , đổi code để lấy accesstoken....
                .oauth2Login(Customizer.withDefaults())
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .logout(logout -> logout
                        .logoutSuccessHandler(this.serverLogoutSuccessHandler())
                )
                .build();
    }

    private ServerLogoutSuccessHandler serverLogoutSuccessHandler() {
        // hủy seasion bên keycloak đi nữa
        OidcClientInitiatedServerLogoutSuccessHandler oidcClientInitiatedServerLogoutSuccessHandler =
                new OidcClientInitiatedServerLogoutSuccessHandler(this.clientRegistrationRepository);
        oidcClientInitiatedServerLogoutSuccessHandler.setPostLogoutRedirectUri("http://frontend:3000");
        return oidcClientInitiatedServerLogoutSuccessHandler;
    }

    // mapper sang quyền từ oauth2loggin() , còn đối tượng authentication được tạo theo mặc định sub=>name
    @Bean
  // bean này mapper quyền ở tầng cuối cùng , nhận lại các quyền trong authentication rồi map lại
    public GrantedAuthoritiesMapper grantedAuthoritiesMapper(){
        // tham số ds quyền mặc định chk phải là quyền thực , mà là chua một trong hai đối tượng check instant dưới chứa thông tin cả idtoken hoặc access token
        return  (authorities)->{
            Set<GrantedAuthority> grantedAuthoritySet = new HashSet<>();
            GrantedAuthority authority = authorities.iterator().next();
            boolean isOidc = authority instanceof OidcUserAuthority;
            if(isOidc){
                OidcUserAuthority oidcUserAuthority = (OidcUserAuthority)authority;
                OidcUserInfo oidcUserInfo = oidcUserAuthority.getUserInfo();
                if(oidcUserInfo.hasClaim(this.REALM_ACCESS_CLAIM)){
                    Map<String, Object> realmAccessClaim = oidcUserInfo.getClaimAsMap(this.REALM_ACCESS_CLAIM);
                    Collection<String> roles = (Collection<String>) realmAccessClaim.get(this.ROLES_CLAIM);
                    grantedAuthoritySet.addAll(this.buildAuthoritiesFromClaimRoles(roles));
                }
                // nếu không phải OIDC
            }else{
                OAuth2UserAuthority oAuth2UserAuthority = (OAuth2UserAuthority)authority;
                Map<String ,Object> userAttributes = oAuth2UserAuthority.getAttributes();
                if(userAttributes.containsKey(this.REALM_ACCESS_CLAIM)){
                    Map<String,Object> realmAccessClaim = (Map<String, Object>) userAttributes.get(this.REALM_ACCESS_CLAIM);
                    Collection<String> roles = (Collection<String>) realmAccessClaim.get(this.ROLES_CLAIM);
                    grantedAuthoritySet.addAll(this.buildAuthoritiesFromClaimRoles(roles));
                }
            }
            return grantedAuthoritySet;
        };
    }
    private Collection<GrantedAuthority> buildAuthoritiesFromClaimRoles(Collection<String> roles){
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_".concat(role)))
                .collect(Collectors.toList());
    }


}


// doc:
//https://docs.spring.io/spring-security/reference/reactive/authentication/x509.html
// nếu oidc thì thông tin đuược mapper vào OidcUserAuthority , còn không thì OAuth2UserAuthority
