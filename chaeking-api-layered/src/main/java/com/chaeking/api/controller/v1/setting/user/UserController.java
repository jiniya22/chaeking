package com.chaeking.api.controller.v1.setting.user;

import com.chaeking.api.domain.value.BaseValue;
import com.chaeking.api.domain.value.TokenValue;
import com.chaeking.api.domain.value.UserValue;
import com.chaeking.api.domain.value.response.BaseResponse;
import com.chaeking.api.domain.value.response.DataResponse;
import com.chaeking.api.service.UserService;
import com.chaeking.api.util.BasicUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Tag(name = "setting-user", description = "설정-사용자")
@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "사용자 정보 조회")
    @GetMapping("")
    public DataResponse<UserValue.Res.Detail> selectOne() {
        return DataResponse.of(userService.selectDetail(BasicUtils.getUserId()));
    }

    @Operation(summary = "사용자 정보 수정 - 이메일 or 닉네임 or 푸시 동의 or 야간 푸시 동의",
            description = """
                    <ul>
                        <li>email, nickname, push, night_push 를 수정합니다.</li>
                        <li>수정하고자 하는 값만 설정하면 됩니다.</li>
                        <li>email 은 email 형식에 맞지 않을 경우 Exception 을 발생시킵니다.</li>
                    </ul>
                    """)
    @PatchMapping("")
    public BaseResponse patchUser(@RequestBody @Valid UserValue.Req.Modification req) {
        userService.patch(BasicUtils.getUserId(), req);
        return BaseResponse.SUCCESS_INSTANCE;
    }

    @Operation(summary = "사용자 탈퇴")
    @DeleteMapping("")
    public BaseResponse deactivate() {
        userService.deativate(BasicUtils.getUserId());
        return BaseResponse.SUCCESS_INSTANCE;
    }

    @Operation(summary = "사용자 정보 수정 - 비밀번호",
            description = """
                    <ul>
                        <li>secret_key: uuid 나 32자 이상의 난수값</li>
                        <li>password: secret_key 를 이용하여 비밀번호를 AES 암호화한 값</li>
                    </ul>
                    """)
    @PatchMapping("/password")
    public BaseResponse patchUserPassword(@RequestBody @Valid UserValue.Req.PasswordModification req) {
        userService.patchPassword(BasicUtils.getUserId(), req);
        return BaseResponse.SUCCESS_INSTANCE;
    }

    @Operation(summary = "사용자 정보 수정 - 프로필 사진",
            description = """
                    프로필 사진을 설정합니다.
                    프로필 사진을 지우고 싶은 경우, image 를 비워서 api 를 실행하면 됩니다.
                    """)
    @PatchMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse patchProfileImage(@RequestPart(required = false) MultipartFile image) {
        userService.patchImageUrl(BasicUtils.getUserId(), image);
        return BaseResponse.SUCCESS_INSTANCE;
    }

    @Operation(summary = "사용자가 좋아하는 작가 목록 조회")
    @GetMapping("/favorite-authors")
    public DataResponse<List<BaseValue>> userAuthors() {
        return DataResponse.of(userService.selectAllUserAndAuthor(BasicUtils.getUserId()));
    }

}
