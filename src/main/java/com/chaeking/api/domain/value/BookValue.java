package com.chaeking.api.domain.value;

import com.chaeking.api.domain.entity.Book;
import com.chaeking.api.util.DateUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Optional;

public final class BookValue {

    public final static class Req {
        public record Creation(
                @NotBlank @Size(max = 500) String name,
                @NotBlank @Size(max = 300) String author,
                int price,
                @NotBlank @Size(max = 300) String publisher,
                @NotBlank @Pattern(regexp = "^(19[0-9][0-9]|20\\d{2})-(0[0-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$") String publication_date,
                @Size(max = 20) String isbn,
                @Size(max = 500) String image_url,
                String detail_info
        ) {
        }
    }

    public final static class Res {
        public record Detail(
                long id,
                String name,
                String author,
                int price,
                String publisher,
                String publication_date,
                String isbn,
                String image_url,
                String detail_info
        ) {
            public Detail(Book b) {
                this(b.getId(), b.getName(), b.getAuthor(), b.getPrice(), b.getPublisher(),
                        Optional.ofNullable(b.getPublicationDate()).map(m -> m.format(DateUtils.DATE_FORMATTER)).orElse(null),
                        b.getIsbn(), b.getImageUrl(), b.getDetailInfo());
            }
        }

    }
}
