package com.uplog_project.backend.api.user.service;

import com.uplog_project.backend.api.global.dto.HasEmail;
import com.uplog_project.backend.api.user.dto.UserBackupDTO;
import com.uplog_project.backend.api.user.dto.UserRequest;

public interface UserService {

    boolean isUserExists(HasEmail userRequest);

    String normalJoinUser(String email);
    void addUser(UserRequest userRequest);

    void deleteUser(Long userId);
}
