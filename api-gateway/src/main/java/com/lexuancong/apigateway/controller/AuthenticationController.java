package com.lexuancong.apigateway.controller;

import com.lexuancong.apigateway.viewmodel.AuthenticatedUserVm;
import com.lexuancong.apigateway.viewmodel.AuthenticationInfoVm;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// api lấy thông tin tên user để hiển thị trên thanh header
@RestController
public class AuthenticationController {
    @GetMapping("/authentication")
    public ResponseEntity<AuthenticationInfoVm> getAuthenticationInfo(@AuthenticationPrincipal OAuth2User principal) {
        if(principal == null) {
            return ResponseEntity.ok(new AuthenticationInfoVm(false, null));
        }
        String username = principal.getAttribute("preferred_username");
        AuthenticatedUserVm authenticatedUse = new AuthenticatedUserVm(username);
        return ResponseEntity.ok(new AuthenticationInfoVm(true, authenticatedUse));
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
