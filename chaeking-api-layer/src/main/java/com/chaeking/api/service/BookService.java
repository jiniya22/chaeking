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

    @Transactional
    public BookValue.Res.Detail book(String isbn13, Long userId) {
        Optional<Book> oBook = bookRepository.findTopWithPublisherByIsbn13(isbn13);
        if(oBook.isPresent()) {
            return getBookDetail(oBook.get(), userId);
        } else {
            KakaoBookValue.Req.Search kakaoBookSearch = KakaoBookValue.Req.Search.builder()
                    .query(isbn13)
                    .target("isbn")
                    .sort("accuracy")
                    .page(1)
                    .size(1).build();
            var responseEntity
                    = kakaoApiClient.searchBooks("KakaoAK " + BookSearchConfig.Kakao.getApiKey(), kakaoBookSearch);
            if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
                return getBookDetail(Objects.requireNonNull(responseEntity.getBody()).getDocuments().stream().findFirst().map(i -> {
                    Book b = i.toBook();
                    Publisher publisher = publisherRepository.findByName(i.getPublisher()).orElse(new Publisher(i.getPublisher()));
                    b.setPublisher(publisher);
                    List<Author> authors = new ArrayList<>();
                    List<BookAndAuthor> bookAndAuthors = new ArrayList<>();
                    i.getAuthors().forEach(authorName -> {
                        Author author = authorRepository.findByName(authorName).orElse(new Author(authorName));
                        authors.add(author);
                        bookAndAuthors.add(BookAndAuthor.of(b, author));
                    });
                    authorRepository.saveAll(authors);
                    b.setBookAndAuthors(bookAndAuthors);
                    bookRepository.save(b);
                    bookAndAuthorRepository.saveAll(bookAndAuthors);
                    return b;
                }).orElse(null), userId);
            }
        }
        return null;
    }

    private BookValue.Res.Detail getBookDetail(Book book, Long userId) {
        if(book == null)
            return null;
        if(userId == null)
            return Book.createDetail(book, null, null);
        BookMemoryComplete bookMemoryComplete = bookMemoryCompleteRepository.findByBookAndUserId(book, userId).orElse(null);
        BookMemoryWish bookMemoryWish = bookMemoryWishRepository.findByBookAndUserId(book, userId).orElse(null);
        return Book.createDetail(book, bookMemoryComplete, bookMemoryWish);
    }

    public BookValue.Res.Detail book(Long bookId, Long userId) {
        return getBookDetail(select(bookId), userId);
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

    public List<BookValue.Res.Kakao> searchKakaoBook(KakaoBookValue.Req.Search kakaoBookSearch) {
        var responseEntity
                = kakaoApiClient.searchBooks("KakaoAK " + BookSearchConfig.Kakao.getApiKey(), kakaoBookSearch);
        if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            return Objects.requireNonNull(responseEntity.getBody()).getDocuments().stream()
                    .map(KakaoBookValue.Res.BookBasic.Document::toKakaoBook).toList();
        }
        return new ArrayList<>();
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
            return bookRepository.findTopWithPublisherByIsbn13(isbn13);
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