package com.chaeking.api.domain.entity;

import com.chaeking.api.model.ContactValue;
import com.chaeking.api.util.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "contact")
@Entity
@Where(clause = "active = 1")
public class Contact extends BaseBoard {

    private long userId;

    @Column(length = 100)
    private String answerTitle;

    @Column(columnDefinition = "TEXT")
    private String answerContent;

    @Builder
    public Contact(String title, String content, long userId) {
        super(title, content);
        this.userId = userId;
    }

    public static ContactValue.Res.Detail createDetail(Contact c) {
        return new ContactValue.Res.Detail(c.getId(), c.getTitle(), DateTimeUtils.toString(c.getCreatedAt()), 
                c.getContent(), Strings.isBlank(c.getAnswerTitle()) ? "답변 대기" : "답변 완료", c.getAnswerTitle(), c.getAnswerContent());
    }

}
