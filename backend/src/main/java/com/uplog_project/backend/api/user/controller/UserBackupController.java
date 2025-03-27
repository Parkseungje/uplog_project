package com.uplog_project.backend.api.user.controller;

import com.uplog_project.backend.api.global.aop.Response;
import com.uplog_project.backend.api.user.dto.UserBackupDTO;
import com.uplog_project.backend.api.user.entity.UserBackup;
import com.uplog_project.backend.api.user.service.UserBackupService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserBackupController {

    @Value("${base-url}")
    private String baseUrl;
    @Value("${google.oauth.redirect-path}")
    private String redirectPath;

    private String redirectUri;

    @PostConstruct
    public void init() {
        this.redirectUri = baseUrl + redirectPath;
    }

    private final UserBackupService userBackupService;

    public UserBackupController(UserBackupService userBackupService) {
        this.userBackupService = userBackupService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Response<?>> signUp(@RequestBody UserBackupDTO userBackupDTO) {
        userBackupService.addUsers(userBackupDTO);
        return ResponseEntity.ok(Response.success("회원가입 성공"));
    }

    @PostMapping("/login")
    public ResponseEntity<Response<?>> logIn(@RequestBody UserBackupDTO userBackupDTO) {
        Map<String, String> data = userBackupService.joinUser(userBackupDTO);
        return ResponseEntity.ok(Response.success(data));
    }

    @GetMapping("/me")
    public ResponseEntity<Response<?>> getMyInfo(Authentication authentication) {
        UserBackup data = userBackupService.findMe(authentication);
        return ResponseEntity.ok(Response.success(data));
    }

    @GetMapping("/oauth/google")
    public void redirectToGoogleOAuth(HttpServletResponse response) throws IOException {
        userBackupService.redirectGoogleRequest(response);
    }

    @PostMapping("/oauth/google/callback")
    public ResponseEntity<Response<?>> requestGoogleAccessToken(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        log.error("✅ 받은 Google OAuth code: {}", code);

        Map<String, String> data = userBackupService.getGoogleAccoutWithToken(code);

        return ResponseEntity.ok(Response.success(data));
    }

}
