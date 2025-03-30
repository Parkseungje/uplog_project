package com.uplog_project.backend.api.user.service;

import com.uplog_project.backend.api.global.dto.HasEmail;
import com.uplog_project.backend.api.user.dto.UserRequest;
import com.uplog_project.backend.api.user.entity.User;
import org.springframework.security.core.Authentication;

public interface UserService {

    boolean isUserExists(HasEmail userRequest);

    User findMe(Authentication authentication);

    String normalJoinUser(String email);
    void addUser(UserRequest userRequest);

    void deleteUser(Long userId);
}
