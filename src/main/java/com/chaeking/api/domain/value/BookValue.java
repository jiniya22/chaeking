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

        @Schema(name = "BookSimple")
        public record Simple(
                long id,
                String name,
                String authors,
                String publisher
        ) {
            public static Simple of(Book b) {
                return new Simple(b.getId(), b.getName(), b.getAuthorNames(), b.getPublisherName());
            }
        }

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
                        DateTimeUtils.toString(b.getPublicationDate()),
                        b.getIsbn(), b.getImageUrl(), b.getDetailInfo(),
                        b.getBookAndAuthors().stream()
                                .map(m -> Optional.ofNullable(m.getAuthor())
                                        .map(Author::getName).orElse(null)).collect(Collectors.toList()));
            }
        }

    }
}
