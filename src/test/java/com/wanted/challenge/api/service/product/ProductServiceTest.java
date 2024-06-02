package com.wanted.challenge.api.service.product;

import com.wanted.challenge.IntegrationTestSupport;
import com.wanted.challenge.api.service.product.request.ProductCreateServiceRequest;
import com.wanted.challenge.api.service.product.response.ProductCreateResponse;
import com.wanted.challenge.domain.member.Member;
import com.wanted.challenge.domain.member.repository.MemberRepository;
import com.wanted.challenge.domain.product.Product;
import com.wanted.challenge.domain.product.ProductStatus;
import com.wanted.challenge.domain.product.repository.ProductRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static com.wanted.challenge.common.message.ExceptionMessage.NO_SUCH_MEMBER;
import static com.wanted.challenge.common.message.ValidationMessage.OUT_OF_LENGTH_PRODUCT_NAME;
import static org.assertj.core.api.Assertions.*;

class ProductServiceTest extends IntegrationTestSupport {

    @Autowired
    private ProductService productService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("입력 받은 제품명의 길이가 100자 초과하면 예외가 발생한다.")
    @Test
    void createProductWithOutOfMaxLengthByProductName() {
        //given
        String memberKey = UUID.randomUUID().toString();
        Member member = createMember(memberKey);

        ProductCreateServiceRequest request = ProductCreateServiceRequest.builder()
            .productName("a".repeat(101))
            .productPrice(1_200_000)
            .build();

        //when
        assertThatThrownBy(() -> productService.createProduct(memberKey, request))
            .isInstanceOf(ConstraintViolationException.class)
            .hasMessage("createProduct.request.productName: " + OUT_OF_LENGTH_PRODUCT_NAME);

        //then
        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(0);
    }

    @DisplayName("입력 받은 회원 고유키가 일치하는 회원이 존재하지 않으면 예외가 발생한다.")
    @Test
    void createProductWithNotMatchMemberKey() {
        //given
        String memberKey = UUID.randomUUID().toString();

        ProductCreateServiceRequest request = ProductCreateServiceRequest.builder()
            .productName("MacBook Air 15 M2 256GB 스페이스그레이")
            .productPrice(1_200_000)
            .build();

        //when
        assertThatThrownBy(() -> productService.createProduct(memberKey, request))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage(NO_SUCH_MEMBER);

        //then
        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(0);
    }

    @DisplayName("제품 정보를 입력 받아 제품 신규 등록을 한다.")
    @Test
    void createProduct() {
        //given
        String memberKey = UUID.randomUUID().toString();
        Member member = createMember(memberKey);

        ProductCreateServiceRequest request = ProductCreateServiceRequest.builder()
            .productName("MacBook Air 15 M2 256GB 스페이스그레이")
            .productPrice(1_200_000)
            .build();

        //when
        ProductCreateResponse response = productService.createProduct(memberKey, request);

        //then
        assertThat(response)
            .isNotNull()
            .hasFieldOrPropertyWithValue("productName", "MacBook Air 15 M2 256GB 스페이스그레이")
            .hasFieldOrPropertyWithValue("productPrice", 1_200_000);

        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(1)
            .extracting("productName", "price", "status")
            .containsExactlyInAnyOrder(
                tuple("MacBook Air 15 M2 256GB 스페이스그레이", 1_200_000, ProductStatus.SELLING)
            );
    }

    private Member createMember(String memberKey) {
        Member member = Member.builder()
            .isDeleted(false)
            .memberKey(memberKey)
            .email("wanted@gmail.com")
            .pwd("wanted1234!")
            .name("원티드")
            .build();
        return memberRepository.save(member);
    }
}