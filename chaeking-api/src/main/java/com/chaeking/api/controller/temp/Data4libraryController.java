package com.chaeking.api.controller.temp;

import com.chaeking.api.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirements
@Tag(name = "data4library", description = "(테스트용) 정보나루")
@RequiredArgsConstructor
@RequestMapping("/data4library")
@RestController
public class Data4libraryController {
    private final LibraryService libraryService;

    @Operation(summary = "도서관 조회",
            description = "정보나루 API를 이용하여 도서관을 조회합니다.")
    @GetMapping("")
    public Object bestSellerTop10(@RequestParam(defaultValue = "11") String region) {
        return libraryService.mergeLibrary(region);
    }
}
