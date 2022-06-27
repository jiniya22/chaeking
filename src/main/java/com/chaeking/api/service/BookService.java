package com.chaeking.api.service;

import com.chaeking.api.config.exception.InvalidInputException;
import com.chaeking.api.domain.entity.*;
import com.chaeking.api.domain.enumerate.KakaoBookSort;
import com.chaeking.api.domain.enumerate.KakaoBookTarget;
import com.chaeking.api.domain.value.BookValue;
import com.chaeking.api.domain.value.naver.KakaoBookValue;
import com.chaeking.api.domain.value.naver.NaverBookValue;
import com.chaeking.api.repository.*;
import com.chaeking.api.util.resttemplate.KakaoApiRestTemplate;
import com.chaeking.api.util.resttemplate.NaverApiRestTemplate;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookService {
    private final UserRepository userRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final BookAndAuthorRepository bookAndAuthorRepository;
    private final BookRepository bookRepository;
    private final NaverApiRestTemplate naverApiRestTemplate;
    private final KakaoApiRestTemplate kakaoApiRestTemplate;

    public Book select(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new InvalidInputException("조회되는 book 정보가 없습니다(book_id Error)"));
    }

    public List<BookValue.Res.Simple> selectAll(List<Long> ids) {
        return bookRepository.findAllWithPublisherByIdIn(ids).stream().map(book -> {
            book.getBookAndAuthors().forEach(bookAndAuthor -> bookAndAuthor.getAuthor().getName());
            return Book.createSimple(book);
        }).collect(Collectors.toList());
    }

    public BookValue.Res.Detail book(long bookId, Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return Book.createDetail(select(bookId), user);
    }

    @Transactional
    public List<Long> searchNaverBookBasic(String name, String sort, int page, int size) {
        String query = "?query=" + name + "&sort=" + sort + "&sort=" + sort + "&start=" + page + "&display=" + size;
        ResponseEntity<NaverBookValue.Res.BookBasic> responseEntity = naverApiRestTemplate.get("/v1/search/book.json" + query, null, NaverBookValue.Res.BookBasic.class);
        if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            List<Book> books = new ArrayList<>();
            List<BookAndAuthor> bookAndAuthors = new ArrayList<>();
            Map<String, Author> authorMap = new HashMap<>();
            Map<String, Publisher> publisherMap = new HashMap<>();
            Objects.requireNonNull(responseEntity.getBody()).getItems().forEach(i -> {
                Book b = findByIsbn(i.getIsbn()).orElse(i.toBook());
                if (b.getId() == null) {
                    books.add(b);
                    b.getBookAndAuthors().forEach(bookAndAuthor -> authorMap.put(bookAndAuthor.getAuthor().getName(), bookAndAuthor.getAuthor()));
                    Optional.ofNullable(b.getPublisher()).ifPresent(publisher -> publisherMap.put(publisher.getName(), publisher));
                    return;
                }

                if (!publisherMap.containsKey(i.getPublisher()))
                    publisherMap.put(i.getPublisher(), publisherRepository.findByName(i.getPublisher()).orElse(new Publisher(i.getPublisher())));
                Arrays.stream(i.getAuthor().split("\\|")).forEach(author -> {
                    if (!authorMap.containsKey(author))
                        authorMap.put(author, authorRepository.findByName(author).orElse(new Author(author)));
                    bookAndAuthors.add(BookAndAuthor.of(b, authorMap.get(author)));
                });
                books.add(b);
            });
            bookRepository.saveAll(books);
            bookAndAuthorRepository.saveAll(bookAndAuthors);
            return books.stream().mapToLong(Book::getId).boxed().collect(Collectors.toList());
        }
        return null;
    }

    @Transactional
    public List<Long> searchKakaoBook(String search, KakaoBookTarget target, KakaoBookSort sort, int page, int size) {
        String query = "?query=" + search + "&target=" + Optional.ofNullable(target).map(KakaoBookTarget::name).orElse("")
                + "&sort=" + Optional.ofNullable(sort).map(KakaoBookSort::name).orElse("accuracy") + "&page=" + page + "&size=" + size;
        ResponseEntity<KakaoBookValue.Res.BookBasic> responseEntity = kakaoApiRestTemplate.get("/v3/search/book" + query, null, KakaoBookValue.Res.BookBasic.class);
        if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            List<Long> bookIds = new ArrayList<>();
            List<BookAndAuthor> bookAndAuthors = new ArrayList<>();
            Map<String, Author> authorMap = new HashMap<>();
            Map<String, Publisher> publisherMap = new HashMap<>();
            Objects.requireNonNull(responseEntity.getBody()).getDocuments().forEach(i -> {
                Book b = findByIsbn(i.getIsbn()).orElse(i.toBook());
                if (b.getId() != null) {
                    bookIds.add(b.getId());
                    b.getBookAndAuthors().forEach(bookAndAuthor -> authorMap.put(bookAndAuthor.getAuthor().getName(), bookAndAuthor.getAuthor()));
                    Optional.ofNullable(b.getPublisher()).ifPresent(publisher -> publisherMap.put(publisher.getName(), publisher));
                    return;
                }

                if (!publisherMap.containsKey(i.getPublisher()))
                    publisherMap.put(i.getPublisher(), publisherRepository.findByName(i.getPublisher()).orElse(new Publisher(i.getPublisher())));
                b.setPublisher(publisherMap.get(i.getPublisher()));
                i.getAuthors().forEach(author -> {
                    if (!authorMap.containsKey(author))
                        authorMap.put(author, authorRepository.findByName(author).orElse(new Author(author)));
                    bookAndAuthors.add(BookAndAuthor.of(b, authorMap.get(author)));
                });
                bookRepository.save(b);
                bookIds.add(b.getId());
            });
            authorRepository.saveAll(authorMap.values());
            bookAndAuthorRepository.saveAll(bookAndAuthors);
            return bookIds;
        }
        return null;
    }

    private Optional<Book> findByIsbn(String isbn) {
        if (Strings.isBlank(isbn)) return Optional.empty();
        String isbn10 = null, isbn13 = null;
        for (String s : isbn.trim().split(" ")) {
            if (s.length() == 10) {
                isbn10 = s;
            } else if (s.length() == 13) {
                isbn13 = s;
            }
        }
        if (Strings.isBlank(isbn10)) {
            return bookRepository.findTopWithPublisherByIsbn10NullAndIsbn13(isbn13);
        }
        return bookRepository.findTopWithPublisherByIsbn10AndIsbn13(isbn10, isbn13);
    }

}