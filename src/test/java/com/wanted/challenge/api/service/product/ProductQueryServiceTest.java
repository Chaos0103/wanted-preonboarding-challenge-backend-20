package com.wanted.challenge.api.service.product;

import com.wanted.challenge.IntegrationTestSupport;
import com.wanted.challenge.api.PageResponse;
import com.wanted.challenge.domain.member.Member;
import com.wanted.challenge.domain.member.repository.MemberRepository;
import com.wanted.challenge.domain.product.Product;
import com.wanted.challenge.domain.product.ProductStatus;
import com.wanted.challenge.domain.product.repository.ProductRepository;
import com.wanted.challenge.domain.product.repository.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.NoSuchElementException;
import java.util.UUID;

import static com.wanted.challenge.common.message.ExceptionMessage.NO_SUCH_PRODUCT;
import static org.assertj.core.api.Assertions.*;

class ProductQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    private ProductQueryService productQueryService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("검색 조건에 맞는 제품 목록이 없는 경우 빈 배열을 반환한다.")
    @Test
    void searchProductsIsEmpty() {
        //given
        //when
        PageResponse<ProductResponse> response = productQueryService.searchProducts(1, "256GB");

        //then
        assertThat(response)
            .isNotNull()
            .hasFieldOrPropertyWithValue("currentPage", 1)
            .hasFieldOrPropertyWithValue("size", 10)
            .hasFieldOrPropertyWithValue("isFirst", true)
            .hasFieldOrPropertyWithValue("isLast", true);
        assertThat(response.getContent()).isEmpty();
    }

    @DisplayName("검색 조건에 맞는 제품 목록을 조회한다.")
    @Test
    void searchProducts() {
        //given
        String memberKey = UUID.randomUUID().toString();
        Member member = createMember(memberKey);

        Product product1 = createProduct(member, "MacBook Air 15 M2 256GB 스페이스그레이", ProductStatus.SELLING, false);
        Product product2 = createProduct(member, "MacBook Air 15 M2 256GB 실버", ProductStatus.RESERVATION, false);
        Product product3 = createProduct(member, "MacBook Air 15 M2 256GB 미드나이트", ProductStatus.COMPLETION, false);
        Product product4 = createProduct(member, "MacBook Air 15 M2 512GB 스페이스그레이", ProductStatus.SELLING, true);

        //when
        PageResponse<ProductResponse> response = productQueryService.searchProducts(1, "256GB");

        //then
        assertThat(response)
            .isNotNull()
            .hasFieldOrPropertyWithValue("currentPage", 1)
            .hasFieldOrPropertyWithValue("size", 10)
            .hasFieldOrPropertyWithValue("isFirst", true)
            .hasFieldOrPropertyWithValue("isLast", true);
        assertThat(response.getContent()).hasSize(3)
            .extracting("productId", "productName", "productStatus")
            .containsExactly(
                tuple(product3.getId(), "MacBook Air 15 M2 256GB 미드나이트", ProductStatus.COMPLETION.getText()),
                tuple(product2.getId(), "MacBook Air 15 M2 256GB 실버", ProductStatus.RESERVATION.getText()),
                tuple(product1.getId(), "MacBook Air 15 M2 256GB 스페이스그레이", ProductStatus.SELLING.getText())
            );
    }

    @DisplayName("입력 받은 제품 식별키와 일치하는 제품이 존재하지 않으면 예외가 발생한다.")
    @Test
    void searchProductWithoutProduct() {
        //given
        //when
        //then
        assertThatThrownBy(() -> productQueryService.searchProduct(1L))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage(NO_SUCH_PRODUCT);
    }

    @DisplayName("제품 식별키로 제품을 상세 조회한다.")
    @Test
    void searchProduct() {
        //given
        String memberKey = UUID.randomUUID().toString();
        Member member = createMember(memberKey);

        Product product = createProduct(member, "MacBook Air 15 M2 256GB 스페이스그레이", ProductStatus.SELLING, false);

        //when
        ProductResponse response = productQueryService.searchProduct(product.getId());

        //then
        assertThat(response)
            .isNotNull()
            .hasFieldOrPropertyWithValue("productId", product.getId())
            .hasFieldOrPropertyWithValue("productName", product.getProductName())
            .hasFieldOrPropertyWithValue("productPrice", product.getPrice())
            .hasFieldOrPropertyWithValue("productStatus", product.getStatus().getText());
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

    private Product createProduct(Member member, String productName, ProductStatus status, boolean isDeleted) {
        Product product = Product.builder()
            .isDeleted(isDeleted)
            .productName(productName)
            .price(10_000)
            .status(status)
            .member(member)
            .build();
        return productRepository.save(product);
    }
}