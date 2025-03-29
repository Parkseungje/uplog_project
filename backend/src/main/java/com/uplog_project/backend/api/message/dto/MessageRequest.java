package com.uplog_project.backend.api.message.dto;

import com.uplog_project.backend.api.global.dto.HasEmail;
import com.uplog_project.backend.api.user.dto.UserRequest;
import com.uplog_project.backend.api.message.entity.MessageTemplate;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
public class MessageRequest implements HasEmail {
    String toUserId;
    String type;
    String code;

    @Override
    public String getEmail() {
        return toUserId;
    }
}
