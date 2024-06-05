package com.wanted.challenge.domain.product.repository;

import com.wanted.challenge.IntegrationTestSupport;
import com.wanted.challenge.domain.member.Member;
import com.wanted.challenge.domain.member.repository.MemberRepository;
import com.wanted.challenge.domain.product.Product;
import com.wanted.challenge.domain.product.ProductStatus;
import com.wanted.challenge.domain.product.repository.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class ProductQueryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private ProductQueryRepository productQueryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("검색 조건에 맞는 제품 식별키 목록을 조회한다.")
    @Test
    void findProductIdByCond() {
        //given
        String memberKey = UUID.randomUUID().toString();
        Member member = createMember(memberKey);

        Product product1 = createProduct(member, "MacBook Air 15 M2 256GB 스페이스그레이", ProductStatus.SELLING, false);
        Product product2 = createProduct(member, "MacBook Air 15 M2 256GB 실버", ProductStatus.RESERVATION, false);
        Product product3 = createProduct(member, "MacBook Air 15 M2 256GB 미드나이트", ProductStatus.COMPLETION, false);
        Product product4 = createProduct(member, "MacBook Air 15 M2 512GB 스페이스그레이", ProductStatus.SELLING, true);

        PageRequest page = PageRequest.of(0, 10);

        //when
        List<Long> productIds = productQueryRepository.findProductIdByCond("256GB", page);

        //then
        assertThat(productIds).hasSize(3)
            .contains(product1.getId(), product2.getId(), product3.getId());
    }

    @DisplayName("제품 식별키 목록으로 제품 목록을 조회한다.")
    @Test
    void findByIdIn() {
        //given
        String memberKey = UUID.randomUUID().toString();
        Member member = createMember(memberKey);

        Product product1 = createProduct(member, "MacBook Air 15 M2 256GB 스페이스그레이", ProductStatus.SELLING, false);
        Product product2 = createProduct(member, "MacBook Air 15 M2 256GB 실버", ProductStatus.RESERVATION, false);
        Product product3 = createProduct(member, "MacBook Air 15 M2 256GB 미드나이트", ProductStatus.COMPLETION, false);

        List<Long> productIds = List.of(product1.getId(), product2.getId(), product3.getId());

        //when
        List<ProductResponse> content = productQueryRepository.findByIdIn(productIds);

        //then
        assertThat(content).hasSize(3)
            .extracting("productId", "productName", "productStatus")
            .containsExactly(
                tuple(product3.getId(), "MacBook Air 15 M2 256GB 미드나이트", ProductStatus.COMPLETION.getText()),
                tuple(product2.getId(), "MacBook Air 15 M2 256GB 실버", ProductStatus.RESERVATION.getText()),
                tuple(product1.getId(), "MacBook Air 15 M2 256GB 스페이스그레이", ProductStatus.SELLING.getText())
            );
    }

    @DisplayName("검색 조건에 맞는 데이터 수를 조회한다.")
    @Test
    void countByCond() {
        //given
        String memberKey = UUID.randomUUID().toString();
        Member member = createMember(memberKey);

        Product product1 = createProduct(member, "MacBook Air 15 M2 256GB 스페이스그레이", ProductStatus.SELLING, false);
        Product product2 = createProduct(member, "MacBook Air 15 M2 256GB 실버", ProductStatus.RESERVATION, false);
        Product product3 = createProduct(member, "MacBook Air 15 M2 256GB 미드나이트", ProductStatus.COMPLETION, false);

        //when
        long totalCount = productQueryRepository.countByCond("256GB");

        //then
        assertThat(totalCount).isEqualTo(3);
    }

    @DisplayName("제품 식별키로 제품 상세 조회를 한다.")
    @Test
    void findById() {
        //given
        String memberKey = UUID.randomUUID().toString();
        Member member = createMember(memberKey);

        Product product = createProduct(member, "MacBook Air 15 M2 256GB 스페이스그레이", ProductStatus.SELLING, false);

        //when
        Optional<ProductResponse> findProduct = productQueryRepository.findById(product.getId());

        //then
        assertThat(findProduct).isPresent()
            .get()
            .hasFieldOrPropertyWithValue("productName", "MacBook Air 15 M2 256GB 스페이스그레이")
            .hasFieldOrPropertyWithValue("productStatus", ProductStatus.SELLING.getText())
            .hasFieldOrPropertyWithValue("productPrice", 10_000);
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