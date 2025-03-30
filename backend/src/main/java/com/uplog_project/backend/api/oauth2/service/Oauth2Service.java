package com.uplog_project.backend.api.oauth2.service;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public interface Oauth2Service {
    void redirectGoogleRequest(HttpServletResponse response) throws IOException;

    Map<String, String> getGoogleAccoutWithToken(String code);
}
