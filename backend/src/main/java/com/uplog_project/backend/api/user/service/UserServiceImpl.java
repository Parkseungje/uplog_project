package com.uplog_project.backend.api.user.service;

import com.uplog_project.backend.api.user.dto.UserRequest;
import com.uplog_project.backend.api.user.entity.User;
import com.uplog_project.backend.api.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addUsers(UserRequest userRequest) {
        // 비밀번호 확인 실패 시 예외 발생
        if(!checkPassword(userRequest)) throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");

        User builder = User.builder()
                .userEmail(userRequest.getUserEmail())
                .userPw(userRequest.getUserPw())
                .userNickname(userRequest.getUserNickname())
                .userIntroduce(userRequest.getUserIntroduce())
                .build();

        userRepository.save(builder);
    }

    //패스워드 체크
    public boolean checkPassword(UserRequest userRequest){
        return userRequest.getUserPw().equals(userRequest.getUserPwCheck());
    }
}
