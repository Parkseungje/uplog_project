package com.uplog_project.backend.api.user.controller;

import com.uplog_project.backend.api.global.aop.Response;
import com.uplog_project.backend.api.user.dto.UserRequest;
import com.uplog_project.backend.api.user.entity.User;
import com.uplog_project.backend.api.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    private final UserService userservice;

    public UserController(UserService userService) {
        this.userservice = userService;
    }

    @GetMapping("/email-login/{id}")
    public ResponseEntity<Response<?>> logIn(@PathVariable("id") String email) {
        String redirectUrl = userservice.normalJoinUser(email);

        return ResponseEntity.status(302)  // HTTP 302 Redirect
                .header("Location", redirectUrl)
                .build();
    }

    @PostMapping("/")
    public ResponseEntity<Response<?>> signUp(@RequestBody UserRequest userRequest) {
        userservice.addUser(userRequest);
        return ResponseEntity.ok(Response.success("회원가입 성공"));
    }

    @GetMapping("/") //내정보 조회
    public ResponseEntity<Response<?>> getMyInfo(Authentication authentication) {
        User data = userservice.findMe(authentication);
        return ResponseEntity.ok(Response.success(data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteUser(@PathVariable("id") Long userId) {
        userservice.deleteUser(userId);
        return ResponseEntity.ok(Response.success("회원 탈퇴 완료"));
    }

}
