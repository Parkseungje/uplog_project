package com.uplog_project.backend.api.user.service;

import com.uplog_project.backend.api.user.dto.UserBackupDTO;
import com.uplog_project.backend.api.user.entity.UserBackup;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import org.springframework.security.core.Authentication;

public interface UserBackupService {
    void addUsers(UserBackupDTO userBackupDTO);

    Map<String, String> joinUser(UserBackupDTO userBackupDTO);

    UserBackup findMe(Authentication authentication);

    Map<String, String> getGoogleAccoutWithToken(String code);

    void redirectGoogleRequest(HttpServletResponse response) throws IOException;
}
