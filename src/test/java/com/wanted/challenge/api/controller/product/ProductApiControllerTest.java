package com.wanted.challenge.api.controller.product;

import com.wanted.challenge.ControllerTestSupport;
import com.wanted.challenge.api.controller.product.request.ProductCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static com.wanted.challenge.common.message.BindingMessage.NOT_BLANK_PRODUCT_NAME;
import static com.wanted.challenge.common.message.BindingMessage.POSITIVE_PRODUCT_NAME;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductApiControllerTest extends ControllerTestSupport {

    private static final String BASE_URL = "/api/v1/products";

    @DisplayName("신규 제품 등록시 제품명은 필수값이다.")
    @Test
    void createProductWithoutProductName() throws Exception {
        //given
        ProductCreateRequest request = ProductCreateRequest.builder()
            .productName(" ")
            .productPrice(1_200_000)
            .build();

        //when //then
        mockMvc.perform(
                post(BASE_URL)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value(NOT_BLANK_PRODUCT_NAME))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("신규 제품 등록시 제품 가격은 양수이다.")
    @Test
    void createProductWithoutZeroPrice() throws Exception {
        //given
        ProductCreateRequest request = ProductCreateRequest.builder()
            .productName("MacBook Air 15 M2 256GB 스페이스그레이")
            .productPrice(0)
            .build();

        //when //then
        mockMvc.perform(
                post(BASE_URL)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value(POSITIVE_PRODUCT_NAME))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("신규 제품을 등록한다.")
    @Test
    void createProduct() throws Exception {
        //given
        ProductCreateRequest request = ProductCreateRequest.builder()
            .productName("MacBook Air 15 M2 256GB 스페이스그레이")
            .productPrice(1_200_000)
            .build();

        //when //then
        mockMvc.perform(
                post(BASE_URL)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @DisplayName("제품 거래를 시작한다.")
    @Test
    void startTransaction() throws Exception {
        //given //when //then
        mockMvc.perform(
                post(BASE_URL + "/{productId}/start-transaction", 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isOk());
    }
}