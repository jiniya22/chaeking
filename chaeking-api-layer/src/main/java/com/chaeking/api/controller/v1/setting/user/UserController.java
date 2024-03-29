package com.chaeking.api.controller.v1.setting.user;

import com.chaeking.api.model.BaseValue;
import com.chaeking.api.model.UserValue;
import com.chaeking.api.model.response.BaseResponse;
import com.chaeking.api.model.response.DataResponse;
import com.chaeking.api.service.UserService;
import com.chaeking.api.util.BasicUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @Operation(summary = "닉네임 유효성 검사",
            description = """
                    <ul>
                        <li>닉네임이 유효할 경우 success, 유효하지 않을 경우 fail을 리턴합니다</li>
                        <li>닉네임이 중복일 경우 유효하지 않음</li>
                        <li>닉네임이 들어있지 않을 경우 유효하지 않음</li>
                    </ul>
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "423", description = "유효하지 않음") })
    @PostMapping("/nickname-check")
    public ResponseEntity<BaseResponse> checkNickname(@RequestBody @Valid UserValue.Req.Nickname req) {
        String message = userService.checkNickname(BasicUtils.getUserId(), req);
        if(message.isBlank())
            return ResponseEntity.ok(BaseResponse.SUCCESS_INSTANCE);
        return ResponseEntity.status(HttpStatus.LOCKED).body(BaseResponse.create(message));
    }

    @Operation(summary = "이메일 유효성 검사",
            description = """
                    <ul>
                        <li>이메일 유효할 경우 success, 유효하지 않을 경우 fail을 리턴합니다</li>
                        <li>이메일이 중복일 경우 유효하지 않음</li>
                        <li>이메일이 들어있지 않을 경우 유효하지 않음</li>
                        <li>토큰이 들어있을 경우, 본인 이메일이라면 유효하다고 간주합니다.</li>
                    </ul>
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "423", description = "유효하지 않음") })
    @PostMapping("/email-check")
    public ResponseEntity<BaseResponse> checkEmail(@RequestBody @Valid UserValue.Req.UserEmail req) {
        String message = userService.checkEmail(BasicUtils.getUserId(), req);
        if(message.isBlank())
            return ResponseEntity.ok(BaseResponse.SUCCESS_INSTANCE);
        return ResponseEntity.status(HttpStatus.LOCKED).body(BaseResponse.create(message));
    }

    @Operation(summary = "사용자 탈퇴",
            description = """
                    <ul>
                        <li>R001: 콘텐츠가 만족스럽지 않아요</li>
                        <li>R002: 이용이 불편해요</li>
                        <li>R003: 자주 사용하지 않아요</li>
                        <li>R004: 다른 어플을 사용할래요</li>
                        <li>R005: 직접 입력</li>
                    </ul>
                    R001~R004 의 경우 reason을 생략해도 됩니다.
                    """)
    @PostMapping("/deactivate")
    public BaseResponse deactivate(@RequestBody @Valid UserValue.Req.Deativate req) {
        userService.deativate(BasicUtils.getUserId(), req);
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
