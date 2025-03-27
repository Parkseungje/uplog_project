package com.uplog_project.backend.api.user.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String email;
    private String name;
    private String domainName;
    private String introduce;
}
