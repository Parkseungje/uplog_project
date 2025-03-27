package com.uplog_project.backend.api.message.service;

import com.uplog_project.backend.api.message.dto.MessageRequest;
import jakarta.mail.MessagingException;

public interface MessageService {
    void sendMimeMessage(MessageRequest messageRequest) throws MessagingException;
}
