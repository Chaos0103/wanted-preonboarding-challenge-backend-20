package com.wanted.challenge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.challenge.api.controller.member.MemberApiController;
import com.wanted.challenge.api.controller.product.ProductApiController;
import com.wanted.challenge.api.service.member.MemberService;
import com.wanted.challenge.api.service.product.ProductQueryService;
import com.wanted.challenge.api.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WithMockUser
@WebMvcTest(controllers = {MemberApiController.class, ProductApiController.class})
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected ProductService productService;

    @MockBean
    protected ProductQueryService productQueryService;
}