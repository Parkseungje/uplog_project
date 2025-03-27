package com.uplog_project.backend.api.message.repository;

import com.uplog_project.backend.api.message.entity.MessageTemplate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<MessageTemplate, Long> {
    Optional<MessageTemplate> findByTypeAndCode(String type, String code);
}
