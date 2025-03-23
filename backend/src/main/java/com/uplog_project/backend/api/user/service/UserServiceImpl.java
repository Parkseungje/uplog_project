package com.uplog_project.backend.api.user.service;

import com.uplog_project.backend.api.global.exception.CustomException;
import com.uplog_project.backend.api.global.exception.user.UserErrorCode;
import com.uplog_project.backend.api.global.security.JwtTokenProvider;
import com.uplog_project.backend.api.user.dto.GoogleAccountProfileResponse;
import com.uplog_project.backend.api.user.dto.UserDTO;
import com.uplog_project.backend.api.user.entity.User;
import com.uplog_project.backend.api.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final GoogleClient googleClient;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, GoogleClient googleClient) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.googleClient = googleClient;
    }

    @Override
    public void addUsers(UserDTO userDTO) {
        //검증 수행
        verification(userDTO);

        User builder = User.builder()
                .userEmail(userDTO.getUserEmail())
                .userPw(passwordEncoder.encode(userDTO.getUserPw()))
                .userNickname(userDTO.getUserNickname())
                .userIntroduce(userDTO.getUserIntroduce())
                .build();

        userRepository.save(builder);
    }

    // 소셜 회원가입용
    public User addUsers(String email, String nickname, String encodedPassword, String provider) {
        User newUser = User.builder()
                .userEmail(isUserExists(email))
                .userNickname(nickname)
                .userPw(encodedPassword)
                .provider(provider)
                .build();

        return userRepository.save(newUser);
    }

    @Override
    public Map<String, String> joinUser(UserDTO userDTO) {
        //이메일 존재 확인
        User user = getUserByEmail(userDTO.getUserEmail());
        //패스워드 일치 확인
        if(!passwordEncoder.matches(userDTO.getUserPw(), user.getUserPw())) {
            throw new CustomException(UserErrorCode.NOT_MATCH_PASSWORD_CONFIRM);
        }

        //토큰 생성
        String token = jwtTokenProvider.createToken(user.getUserEmail());

        return Map.of("token", token, "userNickname",user.getUserNickname());
    }

    @Override
    public User findMe(Authentication authentication) {
        // JWT에서 추출된 사용자 이메일
        String email = authentication.getName();
        User user = getUserByEmail(email);

        return User.builder()
                .userNickname(user.getUserNickname())
                .userEmail(user.getUserEmail())
                .build();
    }

    private void verification(UserDTO userDTO) {
        // 비밀번호 확인 실패 시 예외 발생
        if (!checkPassword(userDTO)) {
            throw new CustomException(UserErrorCode.NOT_MATCH_PASSWORD_CONFIRM);
        }
        // 이메일 중복시 예외 발생
        if (isUserExists(userDTO)) {
            throw new CustomException(UserErrorCode.DUPLICATE_USER_ID);
        }
    }

    // 유저 존재여부 확인
    public boolean isUserExists(UserDTO userDTO){
        return userRepository.findByUserEmail(userDTO.getUserEmail()).isPresent();
    }

    // 유저 존재여부 확인
    public String isUserExists(String userEmail){
        boolean exists = userRepository.findByUserEmail(userEmail).isPresent();

        if (exists) throw new CustomException(UserErrorCode.DUPLICATE_USER_ID);

        return userEmail;
    }

    // 유저 조회, 없으면 예외
    public User getUserByEmail(String email) {
        return userRepository.findByUserEmail(email)
                .orElseThrow(() -> new CustomException(UserErrorCode.NOT_FOUND_USER));
    }

    //비밀번호와 비밀번호 확인 일치하는지 체크
    public boolean checkPassword(UserDTO userDTO){
        return userDTO.getUserPw().equals(userDTO.getUserPwCheck());
    }

    // 리다이렉트 코드를 이용하여 엑세스토큰을 반환
    public Map<String, String> getGoogleAccoutWithToken(String code){
        GoogleAccountProfileResponse profile = googleClient.getGoogleAccountProfile(code);

        String email = profile.getEmail();
        String nickname = profile.getName(); // 구글에서 가져온 이름을 닉네임으로 설정

        log.error("email = {}",email);

        // 소셜 로그인으로 생성한 비밀번호는 랜덤 UUID + 암호화 처리
        String randomPassword = UUID.randomUUID().toString();
        String encodedPassword = passwordEncoder.encode(randomPassword);


        // 1. 사용자 존재여부 확인후 없으면 가입
        User user = userRepository.findByUserEmail(email)
                .orElseGet(() -> addUsers(email, nickname, encodedPassword, "google"));

        // 2. JWT 토큰 생성
        String token = jwtTokenProvider.createToken(user.getUserEmail());

        // 3. 프론트로 반환할 정보 구성
        return Map.of(
                "token", token,
                "userNickname", user.getUserNickname()
        );
    }

    //구글에 code 요청하는 리다이렉트 반환
    public void redirectGoogleRequest(HttpServletResponse response) throws IOException {
        String url = "https://accounts.google.com/o/oauth2/v2/auth?" +
                "client_id=" + googleClient.getClientId() +
                "&redirect_uri=" + googleClient.getRedirectUri() +
                "&response_type=" + googleClient.getResponseType() +
                "&scope=" + googleClient.getScope() +
                "&access_type=offline";

        response.sendRedirect(url);
    }
}
