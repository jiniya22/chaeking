package com.chaeking.api.user.adapter.in.web;

import com.chaeking.api.common.DataResponse;
import com.chaeking.api.common.annotation.WebAdapter;
import com.chaeking.api.common.util.BasicUtils;
import com.chaeking.api.user.application.port.in.GetUserQuery;
import com.chaeking.api.user.application.port.out.UserDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@WebAdapter
@RequiredArgsConstructor
@Tag(name = "setting-user", description = "설정-사용자")
@RestController
@RequestMapping("/v1/users")
public class GetUserController {

    private final GetUserQuery getUserQuery;

    @Operation(summary = "사용자 정보 조회")
    @GetMapping("")
    public DataResponse<UserDetail> selectOne() {
        return DataResponse.create(getUserQuery.getUserDetail(BasicUtils.getUserId()));
    }

}
