package com.uplog_project.backend.api.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserBackupDTO {
    private String userEmail;
    private String userPw;
    private String userPwCheck;
    private String userNickname;
    private String userIntroduce;
}
