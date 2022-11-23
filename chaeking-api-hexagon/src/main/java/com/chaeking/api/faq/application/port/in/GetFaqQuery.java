package com.chaeking.api.faq.application.port.in;

import com.chaeking.api.faq.domain.Faq;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface GetFaqQuery {

    List<Faq> getFaqs(Pageable pageable);
}
