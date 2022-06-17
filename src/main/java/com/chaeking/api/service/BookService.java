package com.chaeking.api.service;

import com.chaeking.api.config.exception.InvalidInputException;
import com.chaeking.api.domain.entity.Author;
import com.chaeking.api.domain.entity.Book;
import com.chaeking.api.domain.entity.BookAndAuthor;
import com.chaeking.api.domain.value.BookValue;
import com.chaeking.api.domain.value.naver.KakaoBookValue;
import com.chaeking.api.domain.value.naver.NaverBookValue;
import com.chaeking.api.repository.BookRepository;
import com.chaeking.api.util.DateTimeUtils;
import com.chaeking.api.util.resttemplate.KakaoApiRestTemplate;
import com.chaeking.api.util.resttemplate.NaverApiRestTemplate;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookService {
    private final AuthorService authorService;
    private final BookRepository bookRepository;
    private final NaverApiRestTemplate naverApiRestTemplate;
    private final KakaoApiRestTemplate kakaoApiRestTemplate;

    public Book select(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new InvalidInputException("조회되는 book 정보가 없습니다(book_id Error)"));
    }

//    @Transactional
//    public BookValue.Res.Detail insert(BookValue.Req.Creation req) {
//        if (StringUtils.isBlank(req.name()))
//            throw new InvalidInputException("name 은 필수 입력값입니다.");
//
//        Book book = Book.builder()
//                .name(req.name())
//                .isbn(req.isbn())
//                .price(req.price())
////                .author(req.author())
//                .imageUrl(req.imageUrl())
//                .publisher(req.publisher())
//                .detailInfo(req.detailInfo())
//                .publicationDate(Optional.ofNullable(req.publicationDate())
//                        .map(m -> LocalDate.parse(m, DateTimeUtils.FORMATTER_DATE)).orElse(null)).build();
//        bookRepository.save(book);
//
//        return new BookValue.Res.Detail(book);
//    }

    public BookValue.Res.Detail book(long bookId) {
        return new BookValue.Res.Detail(select(bookId));
    }

    @Transactional
    public NaverBookValue.Res.BookBasic searchNaverBookBasic(String name, String sort) {
        String query = "?query=" + name + "&sort=" + sort;
        ResponseEntity<NaverBookValue.Res.BookBasic> responseEntity = naverApiRestTemplate.get("/v1/search/book.json" + query, null, NaverBookValue.Res.BookBasic.class);
        if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            NaverBookValue.Res.BookBasic result = responseEntity.getBody();
            result.getItems().forEach(i -> {
                Book b = bookRepository.findByIsbn(i.getIsbn()).orElse(Book.of(i));
                if (b.getId() == null) {
                    b.setBookAndAuthors(getBookAndAuthors(Arrays.asList(i.getAuthor().split("|"))));
                }
                b.update(i);
                bookRepository.save(b);
            });
            return result;
        }
        return null;
    }

    @Transactional
    public KakaoBookValue.Res.BookBasic searchKakaoBook(String search, String target, String sort) {
        String query = "?query=" + search + "&target=" + target + "&sort=" + sort;
        ResponseEntity<KakaoBookValue.Res.BookBasic> responseEntity = kakaoApiRestTemplate.get("/v3/search/book" + query, null, KakaoBookValue.Res.BookBasic.class);
        if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            KakaoBookValue.Res.BookBasic result = responseEntity.getBody();
            result.getDocuments().forEach(i -> {
                Book b = bookRepository.findByIsbn(i.getIsbn()).orElse(Book.of(i));
                if (b.getId() == null) {
                    b.setBookAndAuthors(getBookAndAuthors(i.getAuthors()));
                }
                b.update(i);
                bookRepository.save(b);
            });
            return result;
        }
        return null;
    }

    List<BookAndAuthor> getBookAndAuthors(List<String> authorNames) {
        List<BookAndAuthor> res = new ArrayList<>();
        List<Author> authors = authorService.findAllByNameIn(authorNames);
        authors.forEach(author -> {
            res.add(new BookAndAuthor(author));
        });
        return res;
    }
}