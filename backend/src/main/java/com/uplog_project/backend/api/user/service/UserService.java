package com.uplog_project.backend.api.user.service;

import com.uplog_project.backend.api.user.dto.UserRequest;
import java.util.Map;
import org.springframework.security.core.Authentication;

public interface UserService {
    void addUsers(UserRequest userRequest);

    Map<String, String> joinUser(UserRequest userRequest);

    Map<String, Object> findMe(Authentication authentication);

}
