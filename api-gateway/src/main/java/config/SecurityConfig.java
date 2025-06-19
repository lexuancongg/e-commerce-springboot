package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;

@Configuration
@EnableWebFluxSecurity
// config theo Reactive application (webFlux)
public class SecurityConfig {
    private final ReactiveClientRegistrationRepository clientRegistrationRepository;

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
        OidcClientInitiatedServerLogoutSuccessHandler oidcClientInitiatedServerLogoutSuccessHandler =
                new OidcClientInitiatedServerLogoutSuccessHandler(this.clientRegistrationRepository);
        oidcClientInitiatedServerLogoutSuccessHandler.setPostLogoutRedirectUri("http://localhost:3000");
        return oidcClientInitiatedServerLogoutSuccessHandler;
    }

    // mapper sang quyền từ oauth2 loggin()
    @Bean
    public GrantedAuthoritiesMapper grantedAuthoritiesMapper(){

    }



}


// doc:
//https://docs.spring.io/spring-security/reference/reactive/authentication/x509.html
