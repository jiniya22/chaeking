package com.chaeking.api.domain.value;

import com.chaeking.api.domain.entity.Author;
import com.chaeking.api.domain.entity.Book;
import com.chaeking.api.domain.entity.Publisher;
import com.chaeking.api.util.DateTimeUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class BookValue {

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
                this(b.getId(), b.getName(), b.getPrice(), Optional.ofNullable(b.getPublisher()).map(Publisher::getName).orElse(null),
                        Optional.ofNullable(b.getPublicationDate()).map(m -> m.format(DateTimeUtils.FORMATTER_DATE)).orElse(null),
                        b.getIsbn(), b.getImageUrl(), b.getDetailInfo(),
                        b.getBookAndAuthors().stream()
                                .map(m -> Optional.ofNullable(m.getAuthor())
                                        .map(Author::getName).orElse(null)).collect(Collectors.toList()));
            }
        }

    }
}
