package com.chaeking.api.domain.value;

import com.chaeking.api.domain.entity.Book;
import com.chaeking.api.util.DateTimeUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Optional;

public final class BookValue {

    public final static class Req {
        @Schema(name = "BookCreation")
        public record Creation(
                @NotBlank @Size(max = 500) String name,
                @NotBlank @Size(max = 300) String author,
                int price,
                @NotBlank @Size(max = 300) String publisher,
                @NotBlank @Pattern(regexp = "^(19[0-9][0-9]|20\\d{2})-(0[0-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$") String publicationDate,
                @Size(max = 20) String isbn,
                @Size(max = 500) String image_url,
                String detail_info
        ) {
        }
    }

    public final static class Res {
        @Schema(name = "BookDetail")
        public record Detail(
                long id,
                String name,
                String author,
                int price,
                String publisher,
                String publicationDate,
                String isbn,
                String imageUrl,
                String detailInfo
        ) {
            public Detail(Book b) {
                this(b.getId(), b.getName(), b.getAuthor(), b.getPrice(), b.getPublisher(),
                        Optional.ofNullable(b.getPublicationDate()).map(m -> m.format(DateTimeUtils.FORMATTER_DATE)).orElse(null),
                        b.getIsbn(), b.getImageUrl(), b.getDetailInfo());
            }
        }

    }
}
