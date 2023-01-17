package com.chaeking.api.notice.adapter.in.web;

import com.chaeking.api.notice.application.port.in.GetNoticeQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
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
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/v1/notices/{id}", 1)
                                .header("Content-Type", "application/json")
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}