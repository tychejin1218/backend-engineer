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
  @DisplayName("findAll_상품 목록 조회")
  @Test
  void testFindAll() {

    // Given & When
    List<Product> productList = productRepository.findAll();

    // Then
    log.debug("productList:[{}]", productList);
    assertFalse(productList.isEmpty());
  }

  @Order(2)
  @DisplayName("findByCategoryOrderByPriceAsc"
      + "_카테고리를 기준으로 상품 목록을 조회 후 가격으로 오름차순 정렬")
  @Test
  void testFindByCategoryOrderByPriceAsc() {

    // Given &
    String category = "top";

    // When
    List<Product> productList = productRepository.findByCategoryOrderByPriceAsc(category);

    // Then
    log.debug("productList:[{}]", productList);
    assertFalse(productList.isEmpty());
  }

}