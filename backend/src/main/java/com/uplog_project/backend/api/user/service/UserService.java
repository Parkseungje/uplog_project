package com.uplog_project.backend.api.user.service;

import com.uplog_project.backend.api.user.dto.UserBackupDTO;
import com.uplog_project.backend.api.user.dto.UserRequest;

public interface UserService {
    void addUsers(UserRequest userRequest);
}
