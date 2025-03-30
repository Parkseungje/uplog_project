package com.uplog_project.backend.api.oauth2.service;

import com.uplog_project.backend.api.global.security.JwtTokenProvider;
import com.uplog_project.backend.api.oauth2.entity.google.GoogleAccountProfileResponse;
import com.uplog_project.backend.api.oauth2.entity.google.GoogleClient;
import com.uplog_project.backend.api.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class Oauth2ServiceImpl implements Oauth2Service{

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final GoogleClient googleClient;
    private final UserRepository userRepository;

    public Oauth2ServiceImpl(ClientRegistrationRepository clientRegistrationRepository, JwtTokenProvider jwtTokenProvider, GoogleClient googleClient, UserRepository userRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.googleClient = googleClient;
        this.userRepository = userRepository;
    }

    @Override
    public void redirectGoogleRequest(HttpServletResponse response) throws IOException {
        ClientRegistration  googleRegistration = clientRegistrationRepository.findByRegistrationId("google");

        String redirectUri = UriComponentsBuilder
                .fromUriString(googleRegistration.getProviderDetails().getAuthorizationUri())
                .queryParam("client_id", googleRegistration.getClientId())
                .queryParam("redirect_uri", googleRegistration.getRedirectUri())
                .queryParam("response_type", "code")
                .queryParam("scope", String.join(" ", googleRegistration.getScopes()))
                .queryParam("access_type", "offline")
                .build()
                .toUriString();

        log.info("🔗 Redirecting to Google OAuth: {}", redirectUri);
        response.sendRedirect(redirectUri);
    }

    // 리다이렉트 코드를 이용하여 엑세스토큰을 반환
    public Map<String, String> getGoogleAccoutWithToken(String code){
        GoogleAccountProfileResponse profile = googleClient.getGoogleAccountProfile(code);

        String email = profile.getEmail();
        String name = profile.getName(); // 구글에서 가져온 이름을 닉네임으로 설정
        String exist = "N";

        if(isUserExists(email)) exist = "Y";

        // JWT 토큰 생성
        String token = jwtTokenProvider.createToken(email,name);

        // 프론트로 반환할 정보 구성
        return Map.of(
                "token", token,
                "exist", exist
        );
    }

    // dto 유저 존재여부 확인
    public boolean isUserExists(String email){
        return userRepository.findByEmail(email).isPresent();
    }
}
