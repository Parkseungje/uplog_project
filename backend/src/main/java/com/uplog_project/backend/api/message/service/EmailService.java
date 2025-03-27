package com.uplog_project.backend.api.message.service;

import com.uplog_project.backend.api.global.exception.CustomException;
import com.uplog_project.backend.api.global.exception.message.MessageErrorCode;
import com.uplog_project.backend.api.message.dto.MessageRequest;
import com.uplog_project.backend.api.message.repository.MessageRepository;
import com.uplog_project.backend.api.message.entity.MessageTemplate;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService implements MessageService {

    @Value("${BASE_URL}")
    private String baseUrl;

    private final JavaMailSender javaMailSender;
    private final MessageRepository messageRepository;

    public EmailService(JavaMailSender javaMailSender, MessageRepository messageRepository) {
        this.javaMailSender = javaMailSender;
        this.messageRepository = messageRepository;
    }

    public String getMessageContent(MessageRequest message) {
        return messageRepository
                .findByTypeAndCode(message.getType(), message.getCode())
                .map(MessageTemplate::getContent)
                .orElseThrow(() -> new CustomException(MessageErrorCode.NOT_FOUND_MESSAGE));
    }

    public String getMessageSubject(MessageRequest message) {
        return messageRepository
                .findByTypeAndCode(message.getType(), message.getCode())
                .map(MessageTemplate::getSubject)
                .orElseThrow(() -> new CustomException(MessageErrorCode.NOT_FOUND_MESSAGE));
    }

    @Override
    public void sendMimeMessage(MessageRequest messageRequest) throws MessagingException {
        // Base64 이메일 인코딩했으나 상대방이 디코딩해서 탈취가능 나중에 jwt토큰 발급등으로 암호화 리팩토링 필요
        String encodedEmail = Base64.getEncoder().encodeToString(messageRequest.getToEmail().getBytes());

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

        //메일 받을 유저
        helper.setTo(messageRequest.getToEmail());
        //메일 제목
        helper.setSubject(getMessageSubject(messageRequest));
        //메일 내용폼에 포맷팅
        String contentTemplate = getMessageContent(messageRequest);
        String content = contentTemplate.formatted(getMessageSubject(messageRequest), getMessageSubject(messageRequest), this.baseUrl, encodedEmail);
        //완료된 내용 폼
        helper.setText(content, true);
        javaMailSender.send(mimeMessage);

        log.info("메일 발송 성공 : {}", messageRequest.getToEmail());
    }
}
