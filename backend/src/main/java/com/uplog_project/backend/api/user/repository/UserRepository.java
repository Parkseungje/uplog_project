package com.uplog_project.backend.api.user.repository;

import com.uplog_project.backend.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserEmail(String email);  // 이메일 존재 여부 체크
}
