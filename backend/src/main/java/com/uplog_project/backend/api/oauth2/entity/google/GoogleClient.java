package com.uplog_project.backend.api.oauth2.entity.google;

import com.uplog_project.backend.api.global.exception.CustomException;
import com.uplog_project.backend.api.global.exception.ouath2.Ouath2ErrorCode;
import com.uplog_project.backend.api.global.exception.user.UserErrorCode;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
@Getter
public class GoogleClient {

    private final RestTemplate restTemplate;
    private final ClientRegistrationRepository clientRegistrationRepository;

    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String accessTokenUrl;

    @Value("${spring.security.oauth2.client.provider.google.user-info-uri}")
    private String profileUrl;

    public GoogleAccountProfileResponse getGoogleAccountProfile(final String code) {
        final String accessToken = requestGoogleAccessToken(code);
        return requestGoogleAccountProfile(accessToken);
    }

    private String requestGoogleAccessToken(final String code) {
        ClientRegistration google = clientRegistrationRepository.findByRegistrationId("google");
        if (google == null) {
            throw new CustomException(Ouath2ErrorCode.INVALID_GOOGLE_CLIENT_CONFIG);
        }

        final String decodedCode = URLDecoder.decode(code, StandardCharsets.UTF_8);

        final HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        GoogleAccessTokenRequest requestBody = new GoogleAccessTokenRequest(
                decodedCode,
                google.getClientId(),
                google.getClientSecret(),
                google.getRedirectUri(),
                "authorization_code"
        );

        HttpEntity<GoogleAccessTokenRequest> requestEntity = new HttpEntity<>(requestBody, headers);

        final GoogleAccessTokenResponse response = restTemplate.exchange(
                accessTokenUrl, HttpMethod.POST, requestEntity, GoogleAccessTokenResponse.class
        ).getBody();


        return Optional.ofNullable(response)
                .orElseThrow(() -> new CustomException(UserErrorCode.NOT_FOUND_ACCESS_TOKEN_RESPONSE))
                .getAccessToken();
    }

    private GoogleAccountProfileResponse requestGoogleAccountProfile(final String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(profileUrl, HttpMethod.GET, entity, GoogleAccountProfileResponse.class)
                .getBody();
    }
}
