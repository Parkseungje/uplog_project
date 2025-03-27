package com.uplog_project.backend.api.user.service;

import com.uplog_project.backend.api.global.exception.CustomException;
import com.uplog_project.backend.api.global.exception.user.UserErrorCode;
import com.uplog_project.backend.api.user.dto.UserBackupDTO;
import com.uplog_project.backend.api.user.dto.UserRequest;
import com.uplog_project.backend.api.user.entity.AlarmSetting;
import com.uplog_project.backend.api.user.entity.AuthProvider;
import com.uplog_project.backend.api.user.entity.User;
import com.uplog_project.backend.api.user.entity.UserBackup;
import com.uplog_project.backend.api.user.repository.UserRepository;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addUsers(UserRequest userRequest) {
        //검증 수행
        verification(userRequest);

        //정적 팩토리 메소드 이용
        User user = User.create(
                userRequest.getName(),
                userRequest.getEmail(),
                userRequest.getDomainName(),
                userRequest.getIntroduce(),
                "email",
                UUID.randomUUID().toString()
        );

        userRepository.save(user);
    }

    private void verification(UserRequest userRequest) {
        // 이메일 중복시 예외 발생
        if (isUserExists(userRequest)) {
            throw new CustomException(UserErrorCode.DUPLICATE_USER_ID);
        }
    }

    // 유저 존재여부 확인
    public boolean isUserExists(UserRequest userRequest){
        return userRepository.findByEmail(userRequest.getEmail()).isPresent();
    }
}
