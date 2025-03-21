package com.uplog_project.backend.api.user.service;

import com.uplog_project.backend.api.global.exception.CustomException;
import com.uplog_project.backend.api.global.exception.user.UserErrorCode;
import com.uplog_project.backend.api.global.security.JwtTokenProvider;
import com.uplog_project.backend.api.user.dto.UserRequest;
import com.uplog_project.backend.api.user.entity.User;
import com.uplog_project.backend.api.user.repository.UserRepository;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addUsers(UserRequest userRequest) {
        //검증 수행
        verification(userRequest);

        User builder = User.builder()
                .userEmail(userRequest.getUserEmail())
                .userPw(passwordEncoder.encode(userRequest.getUserPw()))
                .userNickname(userRequest.getUserNickname())
                .userIntroduce(userRequest.getUserIntroduce())
                .build();

        userRepository.save(builder);
    }

    @Override
    public Map<String, String> joinUser(UserRequest userRequest) {
        //이메일 존재 확인
        User user = userRepository.findByUserEmail(userRequest.getUserEmail())
                .orElseThrow(()->new CustomException(UserErrorCode.NOT_FOUND_USER));
        //패스워드 일치 확인
        if(!passwordEncoder.matches(userRequest.getUserPw(), user.getUserPw())) {
            throw new CustomException(UserErrorCode.NOT_MATCH_PASSWORD_CONFIRM);
        }

        //토큰 생성
        String token = jwtTokenProvider.createToken(user.getUserEmail());

        return Map.of("token", token, "userNickname",user.getUserNickname());
    }

    @Override
    public Map<String, Object> findMe(Authentication authentication) {
        String email = authentication.getName(); // JWT에서 추출된 사용자 이메일

        User user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new CustomException(UserErrorCode.NOT_FOUND_USER));

        Map<String, Object> data = Map.of(
                "username", user.getUserNickname(),
                "email", user.getUserEmail()
        );

        return data;
    }

    private void verification(UserRequest userRequest) {
        // 비밀번호 확인 실패 시 예외 발생
        if (!checkPassword(userRequest)) {
            throw new CustomException(UserErrorCode.NOT_MATCH_PASSWORD_CONFIRM);
        }
        // 이메일 중복시 예외 발생
        if (emailDuplicateCheck(userRequest)) {
            throw new CustomException(UserErrorCode.DUPLICATE_USER_ID);
        }
    }

    public boolean emailDuplicateCheck(UserRequest userRequest){
        return userRepository.findByUserEmail(userRequest.getUserEmail()).isPresent();
    }

    //비밀번호와 비밀번호 확인 일치하는지 체크
    public boolean checkPassword(UserRequest userRequest){
        return userRequest.getUserPw().equals(userRequest.getUserPwCheck());
    }
}
