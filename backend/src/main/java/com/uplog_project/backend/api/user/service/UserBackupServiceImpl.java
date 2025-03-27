package com.uplog_project.backend.api.user.service;

import com.uplog_project.backend.api.global.exception.CustomException;
import com.uplog_project.backend.api.global.exception.user.UserErrorCode;
import com.uplog_project.backend.api.global.security.JwtTokenProvider;
import com.uplog_project.backend.api.user.dto.GoogleAccountProfileResponse;
import com.uplog_project.backend.api.user.dto.UserBackupDTO;
import com.uplog_project.backend.api.user.entity.UserBackup;
import com.uplog_project.backend.api.user.repository.UserBackupRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserBackupServiceImpl implements UserBackupService {

    private final UserBackupRepository userBackupRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final GoogleClient googleClient;

    public UserBackupServiceImpl(UserBackupRepository userBackupRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, GoogleClient googleClient) {
        this.userBackupRepository = userBackupRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.googleClient = googleClient;
    }

    @Override
    public void addUsers(UserBackupDTO userBackupDTO) {
        //검증 수행
        verification(userBackupDTO);

        UserBackup builder = UserBackup.builder()
                .userEmail(userBackupDTO.getUserEmail())
                .userPw(passwordEncoder.encode(userBackupDTO.getUserPw()))
                .userNickname(userBackupDTO.getUserNickname())
                .userIntroduce(userBackupDTO.getUserIntroduce())
                .build();

        userBackupRepository.save(builder);
    }

    // 소셜 회원가입용
    public UserBackup addUsers(String email, String nickname, String encodedPassword, String provider) {
        UserBackup newUserBackup = UserBackup.builder()
                .userEmail(isUserExists(email))
                .userNickname(nickname)
                .userPw(encodedPassword)
                .provider(provider)
                .build();

        return userBackupRepository.save(newUserBackup);
    }

    @Override
    public Map<String, String> joinUser(UserBackupDTO userBackupDTO) {
        //이메일 존재 확인
        UserBackup userBackup = getUserByEmail(userBackupDTO.getUserEmail());
        //패스워드 일치 확인
        if(!passwordEncoder.matches(userBackupDTO.getUserPw(), userBackup.getUserPw())) {
            throw new CustomException(UserErrorCode.NOT_MATCH_PASSWORD_CONFIRM);
        }

        //토큰 생성
        String token = jwtTokenProvider.createToken(userBackup.getUserEmail());

        return Map.of("token", token, "userNickname", userBackup.getUserNickname());
    }

    @Override
    public UserBackup findMe(Authentication authentication) {
        // JWT에서 추출된 사용자 이메일
        String email = authentication.getName();
        UserBackup userBackup = getUserByEmail(email);

        return UserBackup.builder()
                .userNickname(userBackup.getUserNickname())
                .userEmail(userBackup.getUserEmail())
                .build();
    }

    private void verification(UserBackupDTO userBackupDTO) {
        // 비밀번호 확인 실패 시 예외 발생
        if (!checkPassword(userBackupDTO)) {
            throw new CustomException(UserErrorCode.NOT_MATCH_PASSWORD_CONFIRM);
        }
        // 이메일 중복시 예외 발생
        if (isUserExists(userBackupDTO)) {
            throw new CustomException(UserErrorCode.DUPLICATE_USER_ID);
        }
    }

    // 유저 존재여부 확인
    public boolean isUserExists(UserBackupDTO userBackupDTO){
        return userBackupRepository.findByUserEmail(userBackupDTO.getUserEmail()).isPresent();
    }

    // 유저 존재여부 확인
    public String isUserExists(String userEmail){
        boolean exists = userBackupRepository.findByUserEmail(userEmail).isPresent();

        if (exists) throw new CustomException(UserErrorCode.DUPLICATE_USER_ID);

        return userEmail;
    }

    // 유저 조회, 없으면 예외
    public UserBackup getUserByEmail(String email) {
        return userBackupRepository.findByUserEmail(email)
                .orElseThrow(() -> new CustomException(UserErrorCode.NOT_FOUND_USER));
    }

    //비밀번호와 비밀번호 확인 일치하는지 체크
    public boolean checkPassword(UserBackupDTO userBackupDTO){
        return userBackupDTO.getUserPw().equals(userBackupDTO.getUserPwCheck());
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
        UserBackup userBackup = userBackupRepository.findByUserEmail(email)
                .orElseGet(() -> addUsers(email, nickname, encodedPassword, "google"));

        // 2. JWT 토큰 생성
        String token = jwtTokenProvider.createToken(userBackup.getUserEmail());

        // 3. 프론트로 반환할 정보 구성
        return Map.of(
                "token", token,
                "userNickname", userBackup.getUserNickname()
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
