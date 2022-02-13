package xyz.applebox.book.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.applebox.book.domain.dto.data.UserDto;
import xyz.applebox.book.domain.dto.response.BaseResponse;
import xyz.applebox.book.service.UserService;

@Tag(name = "user", description = "사용자")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "사용자 등록(= 회원 가입)")
    @PostMapping("")
    public BaseResponse save(@RequestBody UserDto req) {
        BaseResponse res = userService.save(req);
        return res;
    }
}
