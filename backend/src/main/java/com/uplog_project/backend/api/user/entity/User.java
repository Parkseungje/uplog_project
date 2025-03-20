package com.uplog_project.backend.api.user.entity;

import com.uplog_project.backend.api.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "TBL_USER")
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNo;
    private String userEmail;
    private String userPw;

    @Builder.Default
    @Column(nullable = false)
    private String userStatus="N";
    private String provider;
    private String profileImg;
    private String userNickname;
    private String userIntroduce;
    private String userBlogName;
    private String userFacebook;
    private String userGithub;
    private String userTwitter;
    private String userHomepage;
    @Builder.Default
    @Column(nullable = false)
    private int roleNo=1;
}
