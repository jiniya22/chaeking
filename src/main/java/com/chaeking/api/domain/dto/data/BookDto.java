package com.chaeking.api.domain.dto.data;

import com.chaeking.api.domain.entity.Book;
import com.chaeking.api.util.DateUtils;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDto {

    @Data
    public static class BookReq {
        private String name;
        private String author;
        private int price;
        private String publisher;
        private String publication_date;
        private String isbn;
        private String image_url;
        private String detail_info;
    }

    @Data
    public static class BookRes extends BookReq {
        private long id;

        public BookRes(long id, String name) {
            this.id = id;
            setName(name);
        }
        public BookRes(Book b) {
            this(b.getId(), b.getName());
            setAuthor(b.getAuthor());
            setPrice(b.getPrice());
            setPublisher(b.getPublisher());
            if(b.getPublicationDate() != null)
                setPublication_date(DateUtils.DATE.format(b.getPublicationDate()));
            setIsbn(b.getIsbn());
            setImage_url(b.getImageUrl());
            setDetail_info(b.getDetailInfo());
        }
    }
}
