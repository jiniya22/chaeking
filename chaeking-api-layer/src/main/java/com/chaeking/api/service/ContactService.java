package com.chaeking.api.service;

import com.chaeking.api.config.exception.InvalidInputException;
import com.chaeking.api.domain.entity.BaseBoard;
import com.chaeking.api.domain.entity.Contact;
import com.chaeking.api.domain.repository.ContactRepository;
import com.chaeking.api.model.BoardValue;
import com.chaeking.api.model.ContactValue;
import com.chaeking.api.model.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ContactService {
    private final ContactRepository contactRepository;

    @Transactional
    public Long insert(Long userId, BoardValue.Req.Creation req) {
        Contact contact = contactRepository.save(
                Contact.builder().title(req.title()).content(req.content()).userId(userId).build());
        return contact.getId();
    }

    public Contact select(Long contactId, long userId) {
        return contactRepository.findByIdAndUserId(contactId, userId)
                .orElseThrow(() -> new InvalidInputException("조회되는 contact 정보가 없습니다(contact_id Error)"));
    }

    public Page<Contact> select(long userId, Pageable pageable) {
        return contactRepository.findByUserId(userId, pageable);
    }

    public ContactValue.Res.Detail selectOne(long contactId, Long userId) {
        var contact = select(contactId, userId);
        return Contact.createDetail(contact);
    }

    public PageResponse<BoardValue.Res.Simple> selectAll(Long userId, Pageable pageable) {
        Page<Contact> contactPage = select(userId, pageable);
        return PageResponse.create(
                contactPage.stream().map(BaseBoard::createSimple).collect(Collectors.toList()),
                contactPage.getTotalElements(),
                !contactPage.isLast());
    }
}
