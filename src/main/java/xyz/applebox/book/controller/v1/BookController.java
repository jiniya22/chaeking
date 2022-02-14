package xyz.applebox.book.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.applebox.book.domain.dto.data.BookDto;
import xyz.applebox.book.domain.dto.response.CommonResponse;
import xyz.applebox.book.service.BookService;

@Tag(name = "book", description = "책")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/books")
public class BookController {
    private final BookService bookService;

    @Operation(summary = "책 상세조회")
    @GetMapping("/{bookId}")
    public CommonResponse<BookDto> tags(
            @Parameter(name = "bookId", description = "책 id", in = ParameterIn.PATH, required = true)
            @PathVariable(name = "bookId") long bookId) {
        BookDto data = bookService.book(bookId);
        return new CommonResponse<>(data);
    }
}
