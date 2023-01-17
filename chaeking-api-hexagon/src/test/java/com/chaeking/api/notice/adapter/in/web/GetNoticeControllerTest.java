package com.chaeking.api.notice.adapter.in.web;

import com.chaeking.api.notice.application.port.in.GetNoticeQuery;
import com.chaeking.api.notice.application.port.out.NoticeDetail;
import com.chaeking.api.notice.application.port.out.NoticeSimple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

//@AutoConfigureMockMvc
//@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(controllers = GetNoticeController.class)
class GetNoticeControllerTest {

    MockMvc mockMvc;

    @MockBean
    GetNoticeQuery getNoticeQuery;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new GetNoticeController(getNoticeQuery))
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void notices() throws Exception {
        given(getNoticeQuery.getNoticeSimples(PageRequest.of(0, 3, Sort.by(Sort.Order.desc("id")))))
                .willReturn(List.of(new NoticeSimple(3L, "공지사항 3", "2023-01-15 09:30:00"),
                        new NoticeSimple(2L, "공지사항 2", "2023-01-10 18:00:00"),
                        new NoticeSimple(1L, "공지사항 1", "2023-01-09 09:30:00")));
        
        ResultActions resultActions = mockMvc.perform(
                        MockMvcRequestBuilders.get("/v1/notices")
                                .header("Content-Type", "application/json")
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        resultActions
                .andDo(print())
                .andDo(document("notices-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("size").description("조회할 공지사항 수").optional(),
                                parameterWithName("page").description("페이지").optional()
                        )
                ));
    }

    @Test
    void notice() throws Exception {
        given(getNoticeQuery.getNoticeDetail(1L))
                .willReturn(new NoticeDetail(1L, "공지사항 1", "첫번째 공지사항입니다!!", "2023-01-09"));
        
        ResultActions resultActions = mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/v1/notices/{id}", 1)
                                .header("Content-Type", "application/json")
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        resultActions
                .andDo(print())
                .andDo(document("notice-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("공지사항 id")
                        )
                ));
    }
}