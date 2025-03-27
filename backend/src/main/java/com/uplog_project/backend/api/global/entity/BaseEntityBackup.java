package com.uplog_project.backend.api.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import java.time.LocalDateTime;
import lombok.Getter;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntityBackup {

    @CreatedDate
    @Column(updatable = false, nullable = false)
    protected LocalDateTime createDate;

    @Column(nullable = true)
    protected LocalDateTime deleteDate;
}
