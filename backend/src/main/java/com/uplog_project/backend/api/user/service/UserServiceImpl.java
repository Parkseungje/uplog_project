package com.uplog_project.backend.api.user.service;

import com.uplog_project.backend.api.global.exception.CustomException;
import com.uplog_project.backend.api.global.exception.user.UserErrorCode;
import com.uplog_project.backend.api.user.dto.UserRequest;
import com.uplog_project.backend.api.user.entity.User;
import com.uplog_project.backend.api.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
        return userRepository.existsByUserEmail(userRequest.getUserEmail());
    }

    //패스워드 체크
    public boolean checkPassword(UserRequest userRequest){
        return userRequest.getUserPw().equals(userRequest.getUserPwCheck());
    }
}
