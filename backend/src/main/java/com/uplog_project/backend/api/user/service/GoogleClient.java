package com.uplog_project.backend.api.user.service;

import static java.lang.System.getenv;

import com.uplog_project.backend.api.global.exception.CustomException;
import com.uplog_project.backend.api.global.exception.user.UserErrorCode;
import com.uplog_project.backend.api.user.dto.GoogleAccessTokenRequest;
import com.uplog_project.backend.api.user.dto.GoogleAccessTokenResponse;
import com.uplog_project.backend.api.user.dto.GoogleAccountProfileResponse;
import jakarta.annotation.PostConstruct;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import javax.security.auth.login.LoginException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleClient {
    @Value("${base-url}")
    private String baseUrl;
    @Value("${google.oauth.redirect-path}")
    private String redirectPath;

    private String redirectUri;

    @Value("${google.oauth.client-id}")
    private String clientId;
    @Value("${google.oauth.client-secret}")
    private String clientSecret;
    @Value("${google.oauth.authorization-code}")
    private String authorizationCode;
    @Value("${google.oauth.access-token-url}")
    private String accessTokenUrl;
    @Value("${google.oauth.profile-url}")
    private String profileUrl;

    @PostConstruct
    public void init() {
        this.redirectUri = baseUrl + redirectPath;
    }

    private final RestTemplate restTemplate;

    public GoogleAccountProfileResponse getGoogleAccountProfile(final String code) {
        final String accessToken = requestGoogleAccessToken(code);
        return requestGoogleAccountProfile(accessToken);
    }

    private String requestGoogleAccessToken(final String code) {
        log.info("🔁 access token 요청 시작: code = {}", code);
        final String decodedCode = URLDecoder.decode(code, StandardCharsets.UTF_8);
        log.info("✅ 디코딩된 code = {}", decodedCode);

        final HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        final HttpEntity<GoogleAccessTokenRequest> httpEntity = new HttpEntity<>(
                new GoogleAccessTokenRequest(decodedCode, clientId, clientSecret, redirectUri, authorizationCode),
                headers
        );
        log.info("📦 구글 토큰 응답 = {}", httpEntity.getBody());

        final GoogleAccessTokenResponse response = restTemplate.exchange(
                accessTokenUrl, HttpMethod.POST, httpEntity, GoogleAccessTokenResponse.class
        ).getBody();


        return Optional.ofNullable(response)
                .orElseThrow(() -> new CustomException(UserErrorCode.NOT_FOUND_ACCESS_TOKEN_RESPONSE))
                .getAccessToken();
    }

    private GoogleAccountProfileResponse requestGoogleAccountProfile(final String accessToken) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        final HttpEntity<GoogleAccessTokenRequest> httpEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(profileUrl, HttpMethod.GET, httpEntity, GoogleAccountProfileResponse.class)
                .getBody();
    }
}