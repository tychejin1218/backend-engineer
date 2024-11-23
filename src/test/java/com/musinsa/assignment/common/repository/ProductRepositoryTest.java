package com.musinsa.assignment.common.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;

import com.musinsa.assignment.product.entity.Product;
import com.musinsa.assignment.product.repository.ProductRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class ProductRepositoryTest {

  @Autowired
  private ProductRepository productRepository;

  @Order(1)
  @DisplayName("findAll_Product 목록 조회")
  @Test
  void testFindAll() {

    // Given & When
    List<Product> products = productRepository.findAll();

    // Then
    log.debug("products:[{}]", products);
    assertFalse(products.isEmpty());
  }
}