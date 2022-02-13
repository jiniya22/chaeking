package xyz.applebox.book.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.applebox.book.domain.dto.data.BaseDto;
import xyz.applebox.book.domain.dto.data.UserDto;
import xyz.applebox.book.domain.dto.response.BaseResponse;
import xyz.applebox.book.domain.dto.response.CommonResponse;
import xyz.applebox.book.service.TagService;
import xyz.applebox.book.service.UserService;

import java.util.List;

@Tag(name = "tag", description = "태그")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/tags")
public class TagController {

    private final TagService tagService;

    @Operation(summary = "태그 목록")
    @GetMapping("")
    public CommonResponse<List<BaseDto>> tags() {
        List<BaseDto> data = tagService.tags();
        return new CommonResponse<>(data);
    }
}
