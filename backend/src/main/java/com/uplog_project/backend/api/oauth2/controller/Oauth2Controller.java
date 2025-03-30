package com.uplog_project.backend.api.oauth2.controller;

import com.uplog_project.backend.api.global.aop.Response;
import com.uplog_project.backend.api.oauth2.service.Oauth2Service;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauth2")
@Slf4j
public class Oauth2Controller {

    private final Oauth2Service oauth2Service;

    public Oauth2Controller(Oauth2Service oauth2Service) {
        this.oauth2Service = oauth2Service;
    }

    @GetMapping("/google")
    public void redirectToGoogleOAuth(HttpServletResponse response) throws IOException {
        oauth2Service.redirectGoogleRequest(response);
    }

    @PostMapping("/google/callback")
    public ResponseEntity<Response<?>> requestGoogleAccessToken(@RequestBody Map<String, String> body) {
        String code = body.get("code");

        Map<String, String> data = oauth2Service.getGoogleAccoutWithToken(code);

        return ResponseEntity.ok(Response.success(data));
    }
}
