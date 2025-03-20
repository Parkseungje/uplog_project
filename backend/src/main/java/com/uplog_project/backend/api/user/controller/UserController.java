package com.uplog_project.backend.api.user.controller;

import com.uplog_project.backend.api.user.dto.UserRequest;
import com.uplog_project.backend.api.user.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/signup")
    public void signUp(@RequestBody UserRequest userRequest){
        userService.addUsers(userRequest);
    }

}
