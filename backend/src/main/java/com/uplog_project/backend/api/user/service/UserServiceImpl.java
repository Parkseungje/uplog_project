package com.uplog_project.backend.api.user.service;

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
        // 비밀번호 확인 실패 시 예외 발생
        if(!checkPassword(userRequest)) throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");

        User builder = User.builder()
                .userEmail(userRequest.getUserEmail())
//                .userPw(passwordEncoder.encode(userRequest.getUserPw()))
                .userPw(userRequest.getUserPw())
                .userNickname(userRequest.getUserNickname())
                .userIntroduce(userRequest.getUserIntroduce())
                .build();

        System.out.println("userRequest.email = " + userRequest.getUserEmail());
        System.out.println("userRequest.pw = " + userRequest.getUserPw());
        System.out.println("userRequest.pwcheck = " + userRequest.getUserPwCheck());

        userRepository.save(builder);
    }

    //패스워드 체크
    public boolean checkPassword(UserRequest userRequest){
        return userRequest.getUserPw().equals(userRequest.getUserPwCheck());
    }
}
