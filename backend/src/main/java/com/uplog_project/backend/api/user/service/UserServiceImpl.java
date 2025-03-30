package com.uplog_project.backend.api.user.service;

import com.uplog_project.backend.api.global.dto.HasEmail;
import com.uplog_project.backend.api.global.exception.CustomException;
import com.uplog_project.backend.api.global.exception.user.UserErrorCode;
import com.uplog_project.backend.api.global.security.JwtTokenProvider;
import com.uplog_project.backend.api.user.dto.UserRequest;
import com.uplog_project.backend.api.user.entity.User;
import com.uplog_project.backend.api.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Base64;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    @Value("${BASE_URL}")
    private String baseUrl;

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public UserServiceImpl(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    @Transactional
    public void deleteUser(Long userId){
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional
    public void addUser(UserRequest userRequest){
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


    public String normalJoinUser(String email){
        User user = getUserByEmail(decodeString(email));

        String token = jwtTokenProvider.createToken(user.getEmail(),user.getName());
        String redirectUrl = baseUrl+"/?token="+token;

        return  redirectUrl;
    }

    @Override
    public User findMe(Authentication authentication) {
        // JWT에서 추출된 사용자 이메일
        String email = authentication.getName();
        User user = getUserByEmail(email);

        return User.builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public String decodeString(String str){
        byte[] decodedBytes = Base64.getDecoder().decode(str);
        return new String(decodedBytes);
    }

    private void verification(UserRequest userRequest) {
        // 이메일 중복시 예외 발생
        if (isUserExists(userRequest)) throw new CustomException(UserErrorCode.DUPLICATE_USER_EMAIL);
        if (isUserDomainExist(userRequest)) throw new CustomException(UserErrorCode.DUPLICATE_USER_DOMAIN);
    }

    // 이메일로 유저 조회, 없으면 예외
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(UserErrorCode.NOT_FOUND_USER));
    }

    // dto 유저 존재여부 확인
    public boolean isUserExists(HasEmail request){
        return userRepository.findByEmail(request.getEmail()).isPresent();
    }

    public boolean isUserDomainExist(UserRequest userRequest){
        return userRepository.findByDomainName(userRequest.getDomainName()).isPresent();
    }
}
