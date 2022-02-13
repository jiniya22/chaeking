package xyz.applebox.book.domain.dto.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BaseDto {
    private long id;
    private String name;
}
