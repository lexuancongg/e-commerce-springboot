package com.lexuancong.apigateway.controller;

import com.lexuancong.apigateway.dto.AuthenticatedUserResponse;
import com.lexuancong.apigateway.dto.AuthenticationGetResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;

import java.security.Principal;

// api lấy thông tin tên user để hiển thị trên thanh header
@RestController
public class AuthenticationController {
    @GetMapping("/authentication")
    public ResponseEntity<AuthenticationGetResponse> getAuthenticationInfo(@AuthenticationPrincipal OAuth2User principal) {
        if(principal == null) {
            return ResponseEntity.ok(new AuthenticationGetResponse(false, null));
        }
        String username = principal.getAttribute("preferred_username");
        AuthenticatedUserResponse authenticatedUse = new AuthenticatedUserResponse(username);
        return ResponseEntity.ok(new AuthenticationGetResponse(true, authenticatedUse));
    }





    @GetMapping("/session-data")
    public void getSessionData(WebSession session, Principal principal) {
        // session id
        String sessionId = session.getId();

        System.out.println(session);
        System.out.println(principal);
        // tất cả attribute trong session
        session.getAttributes().forEach((k,v) -> System.out.println(k + " : " + v));

    }

}
//        Login
// ↓
//        Authentication -> SecurityContext -> Session (SPRING_SECURITY_CONTEXT)
//
//        Request 1
//        ↓
//        Filter lấy SecurityContext từ session -> SecurityContextHolder (ThreadLocal)
// ↓
//        Controller @AuthenticationPrincipal
// ↓
//        Request kết thúc -> SecurityContextHolder.clear()
//
//        Request 2 (cùng session)
//        ↓
//        Filter lại load SecurityContext từ session -> SecurityContextHolder (ThreadLocal mới)
// ↓
//        Controller @AuthenticationPrincipal
