package xyz.applebox.book.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.applebox.book.config.exception.InvalidInputException;
import xyz.applebox.book.domain.dto.data.BookDto;
import xyz.applebox.book.repository.BookRepository;

@RequiredArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookDto book(long bookId) {
        BookDto res = bookRepository.findById(bookId).map(BookDto::new)
                .orElseThrow(() -> new InvalidInputException("조회되는 book 정보가 없습니다."));
        return res;
    }
}
