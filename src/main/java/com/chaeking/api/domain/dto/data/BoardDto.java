package com.chaeking.api.domain.dto.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BoardDto {
    private long id;
    private String title;
    private String content;
}
