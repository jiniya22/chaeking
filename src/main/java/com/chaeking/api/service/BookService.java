package com.chaeking.api.service;

import com.chaeking.api.config.exception.InvalidInputException;
import com.chaeking.api.domain.entity.Book;
import com.chaeking.api.domain.entity.BookAndAuthor;
import com.chaeking.api.domain.value.BookValue;
import com.chaeking.api.domain.value.naver.KakaoBookValue;
import com.chaeking.api.domain.value.naver.NaverBookValue;
import com.chaeking.api.repository.BookAndAuthorRepository;
import com.chaeking.api.repository.BookRepository;
import com.chaeking.api.util.resttemplate.KakaoApiRestTemplate;
import com.chaeking.api.util.resttemplate.NaverApiRestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookService {
    private final AuthorService authorService;
    private final PublisherService publisherService;
    private final BookAndAuthorRepository bookAndAuthorRepository;
    private final BookRepository bookRepository;
    private final NaverApiRestTemplate naverApiRestTemplate;
    private final KakaoApiRestTemplate kakaoApiRestTemplate;

    public Book select(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new InvalidInputException("조회되는 book 정보가 없습니다(book_id Error)"));
    }

    public BookValue.Res.Detail book(long bookId) {
        return new BookValue.Res.Detail(select(bookId));
    }

    @Transactional
    public NaverBookValue.Res.BookBasic searchNaverBookBasic(String name, String sort, int page, int size) {
        String query = "?query=" + name + "&sort=" + sort + "&sort=" + sort; // + "&start=" + page + "&display=" + size;
        ResponseEntity<NaverBookValue.Res.BookBasic> responseEntity = naverApiRestTemplate.get("/v1/search/book.json" + query, null, NaverBookValue.Res.BookBasic.class);
        if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            NaverBookValue.Res.BookBasic result = responseEntity.getBody();
            result.getItems().forEach(i -> {
                Book b = bookRepository.findWithPublisherByIsbn(i.getIsbn()).orElse(Book.of(i));
                bookRepository.save(b);
                if(b.getPublisher() == null) {
                    b.setPublisher(publisherService.findByName(i.getPublisher()));
                }
                if (CollectionUtils.isEmpty(b.getBookAndAuthors())) {
                    bookAndAuthorRepository.saveAll(
                            BookAndAuthor.of(b, authorService.findAllByNameIn(Arrays.asList(i.getAuthor().split("|"))))
                    );
                }
                b.update(i);
            });
            return result;
        }
        return null;
    }

    @Transactional
    public KakaoBookValue.Res.BookBasic searchKakaoBook(String search, String target, String sort, int page, int size) {
        String query = "?query=" + search + "&target=" + target + "&sort=" + sort + "&page=" + page + "&size=" + size;
        ResponseEntity<KakaoBookValue.Res.BookBasic> responseEntity = kakaoApiRestTemplate.get("/v3/search/book" + query, null, KakaoBookValue.Res.BookBasic.class);
        if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            KakaoBookValue.Res.BookBasic result = responseEntity.getBody();
            result.getDocuments().forEach(i -> {
                Book b = bookRepository.findWithPublisherByIsbn(i.getIsbn()).orElse(Book.of(i));
                bookRepository.save(b);
                if(b.getPublisher() == null) {
                    b.setPublisher(publisherService.findByName(i.getPublisher()));
                }
                if (CollectionUtils.isEmpty(b.getBookAndAuthors())) {
                    bookAndAuthorRepository.saveAll(
                            BookAndAuthor.of(b, authorService.findAllByNameIn(i.getAuthors()))
                    );
                }
                b.update(i);
            });
            return result;
        }
        return null;
    }

}