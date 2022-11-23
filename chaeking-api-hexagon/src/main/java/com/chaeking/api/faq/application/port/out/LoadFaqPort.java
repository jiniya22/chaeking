package com.chaeking.api.faq.application.port.out;

import com.chaeking.api.faq.domain.Faq;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface LoadFaqPort {

    List<Faq> loadFaqs(Pageable pageable);

}
