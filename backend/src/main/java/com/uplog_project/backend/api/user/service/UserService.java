package com.uplog_project.backend.api.user.service;

import com.uplog_project.backend.api.user.dto.GoogleAccountProfileResponse;
import com.uplog_project.backend.api.user.dto.UserDTO;
import com.uplog_project.backend.api.user.entity.User;
import java.util.Map;
import org.springframework.security.core.Authentication;

public interface UserService {
    void addUsers(UserDTO userDTO);

    Map<String, String> joinUser(UserDTO userDTO);

    User findMe(Authentication authentication);

    Map<String, String> getGoogleAccoutWithToken(String code);

}
