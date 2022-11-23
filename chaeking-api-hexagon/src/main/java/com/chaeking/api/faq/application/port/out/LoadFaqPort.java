package com.chaeking.api.faq.application.port.out;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface LoadFaqPort {

    List<FaqSimple> loadFaqSimples(Pageable pageable);
    FaqDetail loadFaqDetail(long id);

}
