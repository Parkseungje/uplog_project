package com.uplog_project.backend.api.user.controller;

import com.uplog_project.backend.api.global.aop.Response;
import com.uplog_project.backend.api.user.dto.GoogleAccountProfileResponse;
import com.uplog_project.backend.api.user.dto.UserDTO;
import com.uplog_project.backend.api.user.entity.User;
import com.uplog_project.backend.api.user.service.UserService;
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
public class UserController {

    @Value("${base-url}")
    private String baseUrl;
    @Value("${google.oauth.redirect-path}")
    private String redirectPath;

    private String redirectUri;

    @PostConstruct
    public void init() {
        this.redirectUri = baseUrl + redirectPath;
    }

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Response<?>> signUp(@RequestBody UserDTO userDTO) {
        userService.addUsers(userDTO);
        return ResponseEntity.ok(Response.success("회원가입 성공"));
    }

    @PostMapping("/login")
    public ResponseEntity<Response<?>> logIn(@RequestBody UserDTO userDTO) {
        Map<String, String> data = userService.joinUser(userDTO);
        return ResponseEntity.ok(Response.success(data));
    }

    @GetMapping("/me")
    public ResponseEntity<Response<?>> getMyInfo(Authentication authentication) {
        User data = userService.findMe(authentication);
        return ResponseEntity.ok(Response.success(data));
    }

    @GetMapping("/oauth/google")
    public void redirectToGoogleOAuth(HttpServletResponse response) throws IOException {
        userService.redirectGoogleRequest(response);
    }

    @PostMapping("/oauth/google/callback")
    public ResponseEntity<Response<?>> requestGoogleAccessToken(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        log.error("✅ 받은 Google OAuth code: {}", code);

        Map<String, String> data = userService.getGoogleAccoutWithToken(code);

        return ResponseEntity.ok(Response.success(data));
    }

}
