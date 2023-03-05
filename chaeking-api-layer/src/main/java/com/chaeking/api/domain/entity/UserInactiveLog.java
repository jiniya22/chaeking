package com.chaeking.api.domain.entity;

import com.chaeking.api.model.enumerate.ReasonCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@Table(name = "user_inactive_log")
@Entity
public class UserInactiveLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long userId;

    @Column(length = 4)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'R001'")
    private ReasonCode reasonCode;

    private String reason;

    private LocalDateTime createdAt;

    @Builder
    public UserInactiveLog(long userId, ReasonCode reasonCode, String reason) {
        this.userId = userId;
        this.reasonCode = reasonCode;
        this.reason = reason;
    }

    @PrePersist
    protected void prePersist() {
        if (Strings.isBlank(reason)) {
            this.reason = switch(reasonCode) {
                case R001 -> "콘텐츠가 만족스럽지 않아요";
                case R002 -> "이용이 불편해요";
                case R003 -> "자주 사용하지 않아요";
                case R004 -> "다른 어플을 사용할래요";
                default -> "기타";
            };
        }
        this.createdAt = LocalDateTime.now();
    }
}
