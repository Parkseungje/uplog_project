package com.uplog_project.backend.api.user.repository;

import com.uplog_project.backend.api.user.entity.UserBackup;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBackupRepository extends JpaRepository<UserBackup, Long> {
    Optional<UserBackup> findByUserEmail(String email);  // 이메일 존재 여부 체크
}
