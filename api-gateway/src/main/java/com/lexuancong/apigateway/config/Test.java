package com.lexuancong.apigateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
@Configuration
@RequiredArgsConstructor
public class Test {


//    private final OAuth2AuthorizedClientService authorizedClientService;
//
//    @GetMapping("/call-api")
//    public String callApi(OAuth2AuthenticationToken authToken) {
//        // Lấy OAuth2AuthorizedClient liên kết với user hiện tại
//        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
//                authToken.getAuthorizedClientRegistrationId(),
//                authToken.getName()
//        );
//
//        // Lấy access token
//        String accessToken = client.getAccessToken().getTokenValue();
//
//        // Gọi service khác với header Authorization
//        // e.g., RestTemplate, WebClient, HttpClient...
//        return "Access Token: " + accessToken;
//    }

}
