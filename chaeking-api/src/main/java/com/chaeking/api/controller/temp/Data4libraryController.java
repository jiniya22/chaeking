package com.chaeking.api.controller.temp;

import com.chaeking.api.domain.value.data4library.Data4LibraryLibraryValue;
import com.chaeking.api.domain.value.data4library.Data4LibraryRecommandValue;
import com.chaeking.api.domain.value.response.DataResponse;
import com.chaeking.api.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirements
@Tag(name = "data4library", description = "(테스트용) 정보나루")
@RequiredArgsConstructor
@RequestMapping("/data4library")
@RestController
public class Data4libraryController {
    private final LibraryService libraryService;

    @Operation(summary = "도서관 조회",
            description = "정보나루 API를 이용하여 도서관을 조회합니다.")
    @GetMapping("/libraries")
    public List<Data4LibraryLibraryValue.Res.Response.Lib> libraries(
            @RequestParam(defaultValue = "11") String region,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        Data4LibraryLibraryValue.Req req = Data4LibraryLibraryValue.Req.builder()
                .pageNo(pageNo).pageSize(pageSize).region(region).build();
        return libraryService.selectLibraries(req);
    }

    @Operation(summary = "도서관 업데이트",
            description = "정보나루 API를 이용하여 도서관 정보를 업데이트합니다.")
    @PostMapping("/libraries")
    public DataResponse<Integer> libraries(
            @RequestParam(defaultValue = "11") String region) {
        return DataResponse.of(libraryService.mergeLibrary(region));
    }

    // TODO :: 사용자가 읽은 책을 기반으로 변경해야합니다.
    @Operation(summary = "추천도서 조회",
            description = "정보나루 API를 이용하여 인기대출도서를 조회합니다.")
    @GetMapping("/recommends")
    public List<Data4LibraryRecommandValue.Res.Response.Doc> recommends() {
        return libraryService.recommends();
    }
}
