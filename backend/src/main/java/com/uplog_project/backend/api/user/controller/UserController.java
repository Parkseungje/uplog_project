package com.uplog_project.backend.api.user.controller;

import com.uplog_project.backend.api.global.aop.Response;
import com.uplog_project.backend.api.message.service.MessageService;
import com.uplog_project.backend.api.user.dto.UserRequest;
import com.uplog_project.backend.api.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api2")
@Slf4j
public class UserController {

    private final UserService userservice;

    public UserController(UserService userservice, MessageService messageService) {
        this.userservice = userservice;
    }

    @PostMapping("/user")  //Restful하게 user로 포인트 나눈다
    public ResponseEntity<Response<?>> signUp(@RequestBody UserRequest userRequest) {
        userservice.addUsers(userRequest);
        return ResponseEntity.ok(Response.success("회원가입 성공"));
    }

}
