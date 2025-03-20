package com.uplog_project.backend.api.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
public class UserRequest {
    private String userEmail;
    private String userPw;
    private String userPwCheck;
    private String userNickname;
    private String userIntroduce;
}
