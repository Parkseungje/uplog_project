package com.uplog_project.backend.api.user.entity;

import com.uplog_project.backend.api.global.entity.BaseEntityBackup;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "TBL_USER")
@Builder
public class UserBackup extends BaseEntityBackup {

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
