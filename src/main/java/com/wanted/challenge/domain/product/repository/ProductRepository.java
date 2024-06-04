package com.wanted.challenge.domain.product.repository;

import com.wanted.challenge.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p join fetch p.member m where p.id = :id")
    Optional<Product> findWithMemberById(long id);
}
