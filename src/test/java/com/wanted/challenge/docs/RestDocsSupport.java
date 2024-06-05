package com.wanted.challenge.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wanted.challenge.common.utils.SecurityUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mockStatic;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

@WithMockUser
@ExtendWith(RestDocumentationExtension.class)
public abstract class RestDocsSupport {

    protected MockMvc mockMvc;
    protected ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private static MockedStatic<SecurityUtils> securityUtils;

    @BeforeAll
    static void beforeAll() {
        securityUtils = mockStatic(SecurityUtils.class);
    }

    @AfterAll
    static void afterAll() {
        securityUtils.close();
    }

    @BeforeEach
    void setUp(RestDocumentationContextProvider provider) {
        this.mockMvc = MockMvcBuilders.standaloneSetup(initController())
            .apply(documentationConfiguration(provider))
            .build();
    }

    protected abstract Object initController();
}
