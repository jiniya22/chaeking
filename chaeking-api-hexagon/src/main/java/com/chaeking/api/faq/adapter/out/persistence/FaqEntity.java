package com.chaeking.api.faq.adapter.out.persistence;

import com.chaeking.api.common.BaseEntity;
import com.chaeking.api.common.DateTimeUtils;
import com.chaeking.api.faq.application.port.out.FaqDetail;
import com.chaeking.api.faq.application.port.out.FaqSimple;
import com.chaeking.api.faq.domain.Faq;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "faq")
@Entity
@Where(clause = "active = 1")
class FaqEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String title;

    @Setter
    private String content;

    public Faq mapToFaq() {
        return new Faq(id, title, content, getCreatedAt());
    }

    public FaqSimple mapToFaqSimple() {
        return new FaqSimple(id, title, DateTimeUtils.toDateString(getCreatedAt()));
    }

    public FaqDetail mapToFaqDetail() {
        return new FaqDetail(id, title, content, DateTimeUtils.toString(getCreatedAt()));
    }

}
