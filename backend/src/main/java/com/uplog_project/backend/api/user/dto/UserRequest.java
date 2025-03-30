package com.uplog_project.backend.api.user.dto;

import com.uplog_project.backend.api.global.dto.HasEmail;
import lombok.Data;

@Data
public class UserRequest implements HasEmail {
    private String email;
    private String name;
    private String domainName;
    private String introduce;
    private String provider;
    private String providerUserId;
}
