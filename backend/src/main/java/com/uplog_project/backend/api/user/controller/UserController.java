package com.uplog_project.backend.api.user.controller;

import com.uplog_project.backend.api.global.aop.Response;
import com.uplog_project.backend.api.user.dto.UserRequest;
import com.uplog_project.backend.api.user.service.UserService;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Response<?>> signUp(@RequestBody UserRequest userRequest) {
        userService.addUsers(userRequest);
        return ResponseEntity.ok(Response.success("회원가입 성공"));
    }

    @PostMapping("/login")
    public ResponseEntity<Response<?>> logIn(@RequestBody UserRequest userRequest) {
        Map<String, String> data = userService.joinUser(userRequest);
        return ResponseEntity.ok(Response.success(data));
    }

    @GetMapping("/me")
    public ResponseEntity<Response<?>> getMyInfo(Authentication authentication) {
        Map<String, Object> data = userService.findMe(authentication);
        return ResponseEntity.ok(Response.success(data));
    }

}
