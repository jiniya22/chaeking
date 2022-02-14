package xyz.applebox.book.domain.dto.data;

import lombok.Getter;
import lombok.Setter;
import xyz.applebox.book.domain.entity.Book;

@Getter
@Setter
public class BookDto extends BaseDto {

    private String author;
    private int price;
    private String publisher;
    private String isbn;
    private String image_url;
    private String detail_info;

    public BookDto(long id, String name) {
        super(id, name);
    }
    public BookDto(Book b) {
        this(b.getId(), b.getName());
        this.author = b.getAuthor();
        this.price = b.getPrice();
        this.publisher = b.getPublisher();
        this.isbn = b.getIsbn();
        this.image_url = b.getImageUrl();
        this.detail_info = b.getDetailInfo();
    }
}
