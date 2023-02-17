package com.chaeking.api.service;

import com.chaeking.api.config.exception.InvalidInputException;
import com.chaeking.api.config.vault.BookSearchConfig;
import com.chaeking.api.domain.entity.*;
import com.chaeking.api.domain.repository.*;
import com.chaeking.api.feignclient.KakaoApiClient;
import com.chaeking.api.feignclient.NaverApiClient;
import com.chaeking.api.model.BookValue;
import com.chaeking.api.model.enumerate.KakaoBookSort;
import com.chaeking.api.model.enumerate.KakaoBookTarget;
import com.chaeking.api.model.naver.KakaoBookValue;
import com.chaeking.api.model.naver.NaverBookValue;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookService {
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final BookAndAuthorRepository bookAndAuthorRepository;
    private final BookMemoryCompleteRepository bookMemoryCompleteRepository;
    private final BookMemoryWishRepository bookMemoryWishRepository;
    private final BookRepository bookRepository;
    private final NaverApiClient naverApiClient;
    private final KakaoApiClient kakaoApiClient;

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

    public BookValue.Res.Detail book(Long bookId, Long userId) {
        Book book = select(bookId);
        BookMemoryComplete bookMemoryComplete = bookMemoryCompleteRepository.findByBookAndUserId(book, userId).orElse(null);
        BookMemoryWish bookMemoryWish = bookMemoryWishRepository.findByBookAndUserId(book, userId).orElse(null);
        return Book.createDetail(select(bookId), bookMemoryComplete, bookMemoryWish);
    }

    @Transactional
    public List<Long> searchNaverBookBasic(NaverBookValue.Req.Search naverBookSearch) {
        var responseEntity
                = naverApiClient.searchBooks(BookSearchConfig.Naver.getClientId(), BookSearchConfig.Naver.getClientSecret(), naverBookSearch);
        if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            List<Book> books = new ArrayList<Book>();
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
    public List<Long> searchKakaoBook(KakaoBookValue.Req.Search kakaoBookSearch) {
        var responseEntity
                = kakaoApiClient.searchBooks("KakaoAK " + BookSearchConfig.Kakao.getApiKey(), kakaoBookSearch);
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

    public List<BookValue.Res.Simple> searchTemp(String search, KakaoBookTarget target, KakaoBookSort sort, int page, int size) {
        return bookRepository.findAllWithPublisherBy(PageRequest.of(page, size, Sort.by(Sort.Order.desc("id")))).stream().map(book -> {
            book.getBookAndAuthors().forEach(bookAndAuthor -> bookAndAuthor.getAuthor().getName());
            return Book.createSimple(book);
        }).collect(Collectors.toList());
    }
}