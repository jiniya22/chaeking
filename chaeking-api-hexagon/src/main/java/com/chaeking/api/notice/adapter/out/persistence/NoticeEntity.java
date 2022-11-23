package com.chaeking.api.notice.adapter.out.persistence;

import com.chaeking.api.common.BaseEntity;
import com.chaeking.api.common.DateTimeUtils;
import com.chaeking.api.notice.application.port.out.NoticeSimple;
import com.chaeking.api.notice.domain.Notice;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notice")
@Entity
@Where(clause = "active = 1")
class NoticeEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String title;

    @Setter
    private String content;

    public Notice mapToNotice() {
        return new Notice(id, title, content, getCreatedAt());
    }

    public NoticeSimple mapToNoticeSimple() {
        return new NoticeSimple(id, title, DateTimeUtils.toDateString(getCreatedAt()));
    }
}
