package com.chaeking.api.domain.entity;

import com.chaeking.api.model.TermsValue;
import com.chaeking.api.util.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "terms")
@Entity
public class Terms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String title;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK__TERMS__TERMS_LOG"))
    private TermsLog termsLog;

    public static TermsValue createDetail(Terms t) {
        return new TermsValue(
                t.getTermsLog().getId(),
                t.getTitle(),
                t.getTermsLog().getUrl(),
                DateTimeUtils.toString(t.getTermsLog().getEffectiveOn()));
    }

}
