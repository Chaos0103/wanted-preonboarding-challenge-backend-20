package com.wanted.challenge.docs.product;

import com.wanted.challenge.api.PageResponse;
import com.wanted.challenge.api.controller.product.ProductApiController;
import com.wanted.challenge.api.controller.product.request.ProductCreateRequest;
import com.wanted.challenge.api.controller.product.request.ProductSearchParam;
import com.wanted.challenge.api.service.product.ProductQueryService;
import com.wanted.challenge.api.service.product.ProductService;
import com.wanted.challenge.api.service.product.response.ProductCreateResponse;
import com.wanted.challenge.common.security.SecurityUtils;
import com.wanted.challenge.docs.RestDocsSupport;
import com.wanted.challenge.domain.product.repository.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductApiControllerDocsTest extends RestDocsSupport {

    private static final String BASE_URL = "/api/v1/products";
    private final ProductService productService = mock(ProductService.class);
    private final ProductQueryService productQueryService = mock(ProductQueryService.class);

    @Override
    protected Object initController() {
        return new ProductApiController(productService, productQueryService);
    }

    @DisplayName("제품 신규 등록 API")
    @Test
    void createProduct() throws Exception {
        ProductCreateRequest request = ProductCreateRequest.builder()
            .productName("MacBook Air 15 M2 256GB 스페이스그레이")
            .productPrice(1_200_000)
            .build();

        ProductCreateResponse response = ProductCreateResponse.builder()
            .productId(1L)
            .productName("MacBook Air 15 M2 256GB 스페이스그레이")
            .productPrice(1_200_000)
            .createdDateTime(LocalDateTime.of(2024, 1, 1, 13, 0))
            .build();

        given(SecurityUtils.getCurrentMemberKey())
            .willReturn(UUID.randomUUID().toString());

        given(productService.createProduct(anyString(), any()))
            .willReturn(response);

        mockMvc.perform(
                post(BASE_URL)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "access.token")
            )
            .andDo(print())
            .andExpect(status().isCreated())
            .andDo(document("create-product",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION)
                        .description("JWT 토큰")
                ),
                requestFields(
                    fieldWithPath("productName").type(JsonFieldType.STRING)
                        .description("신규 등록할 제품명"),
                    fieldWithPath("productPrice").type(JsonFieldType.NUMBER)
                        .description("신규 등록할 제품가격")
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
                    fieldWithPath("data.productId").type(JsonFieldType.NUMBER)
                        .description("신규 등록된 제품 식별키"),
                    fieldWithPath("data.productName").type(JsonFieldType.STRING)
                        .description("신규 등록된 제품명"),
                    fieldWithPath("data.productPrice").type(JsonFieldType.NUMBER)
                        .description("신규 등록된 제품 가격"),
                    fieldWithPath("data.createdDateTime").type(JsonFieldType.ARRAY)
                        .description("신규 제품 등록 일시")
                )
            ));
    }

    @DisplayName("제품 목록 조회 API")
    @Test
    void searchProducts() throws Exception {
        ProductSearchParam param = ProductSearchParam.builder()
            .page(1)
            .keyword("MacBook")
            .build();

        ProductResponse response1 = ProductResponse.builder()
            .productId(1L)
            .productName("MacBook Air 15 M2 256GB 스페이스그레이")
            .productPrice(1_200_000)
            .productStatus("판매중")
            .build();
        ProductResponse response2 = ProductResponse.builder()
            .productId(2L)
            .productName("MacBook Air 15 M2 256GB 미트나이트")
            .productPrice(1_200_000)
            .productStatus("예약중")
            .build();
        ProductResponse response3 = ProductResponse.builder()
            .productId(3L)
            .productName("MacBook Air 15 M2 256GB 실버")
            .productPrice(1_200_000)
            .productStatus("완료")
            .build();
        List<ProductResponse> content = List.of(response1, response2, response3);
        PageRequest page = PageRequest.of(0, 10);
        PageResponse<ProductResponse> response = PageResponse.of(new PageImpl<>(content, page, 3));

        given(productQueryService.searchProducts(anyInt(), anyString()))
            .willReturn(response);

        mockMvc.perform(
                get(BASE_URL)
                    .queryParam("page", String.valueOf(param.getPage()))
                    .queryParam("keyword", param.getKeyword())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("search-products",
                preprocessResponse(prettyPrint()),
                queryParameters(
                    parameterWithName("page")
                        .optional()
                        .description("페이지 번호"),
                    parameterWithName("keyword")
                        .optional()
                        .description("조회할 키워드")
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
                    fieldWithPath("data.content[].productStatus").type(JsonFieldType.STRING)
                        .description("조회된 제품 상태"),
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

    @DisplayName("제품 상세 조회 API")
    @Test
    void searchProduct() throws Exception {
        ProductResponse response = ProductResponse.builder()
            .productId(1L)
            .productName("MacBook Air 15 M2 256GB 스페이스그레이")
            .productPrice(1_200_000)
            .productStatus("판매중")
            .build();

        given(productQueryService.searchProduct(anyLong()))
            .willReturn(response);

        mockMvc.perform(
                get(BASE_URL + "/{productId}", 1)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("search-product",
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("productId")
                        .description("제품 식별키")
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
                    fieldWithPath("data.productId").type(JsonFieldType.NUMBER)
                        .description("조회된 제품 식별키"),
                    fieldWithPath("data.productName").type(JsonFieldType.STRING)
                        .description("조회된 제품명"),
                    fieldWithPath("data.productPrice").type(JsonFieldType.NUMBER)
                        .description("조회된 제품 가격"),
                    fieldWithPath("data.productStatus").type(JsonFieldType.STRING)
                        .description("조회된 제품 상태")
                )
            ));
    }

    @DisplayName("제품 거래 시작 API")
    @Test
    void startTransaction() throws Exception {
        ProductResponse response = ProductResponse.builder()
            .productId(1L)
            .productName("MacBook Air 15 M2 256GB 스페이스그레이")
            .productPrice(1_200_000)
            .productStatus("예약중")
            .build();

        given(SecurityUtils.getCurrentMemberKey())
            .willReturn(UUID.randomUUID().toString());

        given(productService.startTransaction(anyString(), anyLong()))
            .willReturn(response);

        mockMvc.perform(
                post(BASE_URL + "/{productId}/start-transaction", 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "access.token")
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("start-transaction-product",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION)
                        .description("JWT 토큰")
                ),
                pathParameters(
                    parameterWithName("productId")
                        .description("제품 식별키")
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
                    fieldWithPath("data.productId").type(JsonFieldType.NUMBER)
                        .description("거래 시작된 제품 식별키"),
                    fieldWithPath("data.productName").type(JsonFieldType.STRING)
                        .description("거래 시작된 제품명"),
                    fieldWithPath("data.productPrice").type(JsonFieldType.NUMBER)
                        .description("거래 시작된 제품 가격"),
                    fieldWithPath("data.productStatus").type(JsonFieldType.STRING)
                        .description("거래 시작된 제품 상태")
                )
            ));
    }

    @DisplayName("제품 판매 승인 API")
    @Test
    void saleApproval() throws Exception {
        ProductResponse response = ProductResponse.builder()
            .productId(1L)
            .productName("MacBook Air 15 M2 256GB 스페이스그레이")
            .productPrice(1_200_000)
            .productStatus("완료")
            .build();

        given(SecurityUtils.getCurrentMemberKey())
            .willReturn(UUID.randomUUID().toString());

        given(productService.saleApproval(anyString(), anyLong(), anyLong()))
            .willReturn(response);

        mockMvc.perform(
                post(BASE_URL + "/{productId}/sale-approval/{orderId}", 1, 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "access.token")
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("sale-approval-product",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION)
                        .description("JWT 토큰")
                ),
                pathParameters(
                    parameterWithName("productId")
                        .description("제품 식별키"),
                    parameterWithName("orderId")
                        .description("주문 식별키")
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
                    fieldWithPath("data.productId").type(JsonFieldType.NUMBER)
                        .description("판매 승인된 제품 식별키"),
                    fieldWithPath("data.productName").type(JsonFieldType.STRING)
                        .description("판매 승인된 제품명"),
                    fieldWithPath("data.productPrice").type(JsonFieldType.NUMBER)
                        .description("판매 승인된 제품 가격"),
                    fieldWithPath("data.productStatus").type(JsonFieldType.STRING)
                        .description("판매 승인된 제품 상태")
                )
            ));
    }
}
