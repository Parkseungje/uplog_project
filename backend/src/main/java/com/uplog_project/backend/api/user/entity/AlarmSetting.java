package com.uplog_project.backend.api.user.entity;

import com.uplog_project.backend.api.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class AlarmSetting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @Builder.Default
    private Boolean commentAlarm = true;

    @Column(nullable = false)
    @Builder.Default
    private Boolean updateAlarm = true;

    public static AlarmSetting create(User user) {
        AlarmSetting alarmSetting = new AlarmSetting();
        alarmSetting.user = user;
        alarmSetting.commentAlarm = true;
        alarmSetting.updateAlarm = true;
        return alarmSetting;
    }
}
