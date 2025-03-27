package com.uplog_project.backend.api.user.entity;

import com.uplog_project.backend.api.global.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "user")
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String domainName;

    private String introduce;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isUsed = true;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isDeleted = false;

    private String profileImageUrl;

    @OneToMany(mappedBy = "user")
    private List<SocialInfo> socialInfos = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private AlarmSetting alarmSetting;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private AuthProvider authProvider;

    // 정적 팩토리 메서드로 생성 책임 명확하게 분리
    public static User create(String name, String email, String domainName, String introduce,
            String provider, String providerUserId) {
        User user = new User(name, email, domainName, introduce);

        // 연관관계 객체도 내부에서 생성 및 연결
        user.alarmSetting = AlarmSetting.create(user);
        user.authProvider = AuthProvider.create(user, provider, providerUserId);

        return user;
    }

    // 내부 생성자 (생성자 오염 방지)
    private User(String name, String email, String domainName, String introduce) {
        this.name = name;
        this.email = email;
        this.domainName = domainName;
        this.introduce = introduce;
        this.isDeleted = false;
        this.isUsed = true;
        this.role = "user";
    }

}
