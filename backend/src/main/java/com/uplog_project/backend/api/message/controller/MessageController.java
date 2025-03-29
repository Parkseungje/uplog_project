package com.uplog_project.backend.api.message.controller;

import com.uplog_project.backend.api.global.aop.Response;
import com.uplog_project.backend.api.message.dto.MessageRequest;
import com.uplog_project.backend.api.message.service.MessageService;
import com.uplog_project.backend.api.user.entity.User;
import com.uplog_project.backend.api.user.service.UserService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api2")
@Slf4j
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/message")
    public ResponseEntity<Response<?>> sendMessage(@RequestBody MessageRequest messageRequest) throws MessagingException {
        messageService.sendMimeMessage(messageRequest);
        return ResponseEntity.ok(Response.success());
    }
}
