package com.chaeking.api.domain.value.data4library;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(value = PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class Book {
    private String bookname;
    private String authors;
    private String publisher;
    private String publicagtion_year;
    private String isbn13;
    private String addition_symbol;
    private String class_nm;
    private String loan_count;
    private String bookImageURL;
}