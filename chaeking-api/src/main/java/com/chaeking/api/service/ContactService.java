package com.chaeking.api.service;

import com.chaeking.api.config.exception.InvalidInputException;
import com.chaeking.api.domain.entity.Contact;
import com.chaeking.api.domain.entity.User;
import com.chaeking.api.domain.value.BoardValue;
import com.chaeking.api.domain.value.ContactValue;
import com.chaeking.api.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ContactService {
    private final ContactRepository contactRepository;
    private final UserService userService;

    @Transactional
    public Long insert(Long userId, BoardValue.Req.Creation req) {
        User user = userService.select(userId);
        Contact contact = contactRepository.save(
                Contact.builder().title(req.title()).content(req.content()).user(user).build());
        return contact.getId();
    }

    public Contact select(Long contactId, User user) {
        return contactRepository.findByIdAndUser(contactId, user)
                .orElseThrow(() -> new InvalidInputException("조회되는 contact 정보가 없습니다(contact_id Error)"));
    }

    public ContactValue.Res.Detail selectOne(long contactId, Long userId) {
        var user = userService.select(userId);
        var contact = select(contactId, user);
        return Contact.createDetail(contact);
    }
}
