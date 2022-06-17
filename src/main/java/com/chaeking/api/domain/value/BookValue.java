package com.chaeking.api.domain.value;

import com.chaeking.api.domain.entity.Author;
import com.chaeking.api.domain.entity.Book;
import com.chaeking.api.util.DateTimeUtils;
import com.chaeking.api.util.RegexpUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class BookValue {

    public final static class Req {
        @Schema(name = "BookCreation")
        public record Creation(
                @NotBlank @Size(max = 500) String name,
                @NotBlank @Size(max = 300) String author,
                int price,
                @NotBlank @Size(max = 300) String publisher,
                @NotBlank @Pattern(regexp = RegexpUtils.DATE) String publicationDate,
                @Size(max = 20) String isbn,
                @Size(max = 500) String imageUrl,
                String detailInfo
        ) {
        }
    }

    public final static class Res {
        @Schema(name = "BookDetail")
        public record Detail(
                long id,
                String name,
                int price,
                String publisher,
                String publicationDate,
                String isbn,
                String imageUrl,
                String detailInfo,
                List<String> authors
        ) {
            public Detail(Book b) {
                this(b.getId(), b.getName(), b.getPrice(), b.getPublisher(),
                        Optional.ofNullable(b.getPublicationDate()).map(m -> m.format(DateTimeUtils.FORMATTER_DATE)).orElse(null),
                        b.getIsbn(), b.getImageUrl(), b.getDetailInfo(),
                        b.getBookAndAuthors().stream()
                                .map(m -> Optional.ofNullable(m.getAuthor())
                                        .map(Author::getName).orElse(null)).collect(Collectors.toList()));
            }
        }

    }
}
