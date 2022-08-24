package com.chaeking.api.service;

import com.chaeking.api.config.exception.InvalidInputException;
import com.chaeking.api.domain.entity.BaseBoard;
import com.chaeking.api.domain.entity.BookMemoryComplete;
import com.chaeking.api.domain.entity.Contact;
import com.chaeking.api.domain.entity.User;
import com.chaeking.api.domain.value.BoardValue;
import com.chaeking.api.domain.value.ContactValue;
import com.chaeking.api.domain.value.response.PageResponse;
import com.chaeking.api.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    public Page<Contact> select(User user, Pageable pageable) {
        return contactRepository.findByUser(user, pageable);
    }

    public ContactValue.Res.Detail selectOne(long contactId, Long userId) {
        var user = userService.select(userId);
        var contact = select(contactId, user);
        return Contact.createDetail(contact);
    }

    public PageResponse<BoardValue.Res.Simple> selectAll(Long userId, Pageable pageable) {
        var user = userService.select(userId);
        Page<Contact> contactPage = select(user, pageable);
        return PageResponse.create(
                contactPage.stream().map(BaseBoard::createSimple).collect(Collectors.toList()),
                contactPage.getTotalElements(),
                !contactPage.isLast());
    }
}
