package com.chaeking.api.domain.dto.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BaseDto {
    private long id;
    private String name;
}
