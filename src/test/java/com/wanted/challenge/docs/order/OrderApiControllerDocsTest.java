package com.wanted.challenge.docs.order;

import com.wanted.challenge.api.PageResponse;
import com.wanted.challenge.api.controller.order.OrderApiController;
import com.wanted.challenge.api.service.order.OrderQueryService;
import com.wanted.challenge.common.utils.SecurityUtils;
import com.wanted.challenge.docs.RestDocsSupport;
import com.wanted.challenge.domain.order.OrderStatus;
import com.wanted.challenge.domain.order.repository.response.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderApiControllerDocsTest extends RestDocsSupport {

    private static final String BASE_URL = "/api/v1/orders";
    private final OrderQueryService orderQueryService = mock(OrderQueryService.class);

    @Override
    protected Object initController() {
        return new OrderApiController(orderQueryService);
    }

    @DisplayName("주문 목록 조회 API")
    @Test
    void searchOrders() throws Exception {
        OrderResponse response1 = OrderResponse.builder()
            .productId(1L)
            .productName("MacBook Air 15 M2 256GB 스페이스그레이")
            .productPrice(1_200_000)
            .orderStatus(OrderStatus.ORDER)
            .orderDateTime(LocalDateTime.now())
            .build();
        OrderResponse response2 = OrderResponse.builder()
            .productId(2L)
            .productName("MacBook Air 15 M2 256GB 미트나이트")
            .productPrice(1_200_000)
            .orderStatus(OrderStatus.COMPLETE)
            .orderDateTime(LocalDateTime.now())
            .build();

        List<OrderResponse> content = List.of(response1, response2);
        PageRequest page = PageRequest.of(0, 10);
        PageResponse<OrderResponse> response = PageResponse.of(new PageImpl<>(content, page, 2));

        given(SecurityUtils.getCurrentMemberKey())
            .willReturn(UUID.randomUUID().toString());

        given(orderQueryService.searchOrders(anyString(), anyInt()))
            .willReturn(response);

        mockMvc.perform(
                get(BASE_URL)
                    .queryParam("page", "1")
                    .header(HttpHeaders.AUTHORIZATION, "access.token")
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("search-orders",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION)
                        .description("JWT 토큰")
                ),
                queryParameters(
                    parameterWithName("page")
                        .optional()
                        .description("페이지 번호")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                        .description("코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING)
                        .description("상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                        .description("응답 데이터"),
                    fieldWithPath("data.content").type(JsonFieldType.ARRAY)
                        .description("조회된 제품 목록"),
                    fieldWithPath("data.content[].productId").type(JsonFieldType.NUMBER)
                        .description("조회된 제품 식별키"),
                    fieldWithPath("data.content[].productName").type(JsonFieldType.STRING)
                        .description("조회된 제품명"),
                    fieldWithPath("data.content[].productPrice").type(JsonFieldType.NUMBER)
                        .description("조회된 제품 가격"),
                    fieldWithPath("data.content[].orderStatus").type(JsonFieldType.STRING)
                        .description("조회된 주문 상태"),
                    fieldWithPath("data.content[].orderDateTime").type(JsonFieldType.ARRAY)
                        .description("조회된 주문 일시"),
                    fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER)
                        .description("현재 페이지"),
                    fieldWithPath("data.size").type(JsonFieldType.NUMBER)
                        .description("조회된 데이터 갯수"),
                    fieldWithPath("data.isFirst").type(JsonFieldType.BOOLEAN)
                        .description("첫 페이지 여부"),
                    fieldWithPath("data.isLast").type(JsonFieldType.BOOLEAN)
                        .description("마지막 페이지 여부")
                )
            ));
    }
}
