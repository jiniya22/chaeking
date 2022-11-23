package com.chaeking.api.faq.application.port.in;

import com.chaeking.api.faq.application.port.out.FaqDetail;
import com.chaeking.api.faq.application.port.out.FaqSimple;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface GetFaqQuery {

    List<FaqSimple> getFaqs(Pageable pageable);

    FaqDetail getFaq(long id);
}
