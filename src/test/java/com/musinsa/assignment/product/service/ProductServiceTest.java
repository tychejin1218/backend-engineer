package com.musinsa.assignment.product.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musinsa.assignment.product.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
class ProductServiceTest {

  @Autowired
  ProductService productService;

  @Autowired
  ObjectMapper objectMapper;

  @Order(1)
  @Transactional
  @DisplayName("getCheapestProductForEachCategory"
      + "_카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회")
  @Test
  void testGetCheapestProductForEachCategory() throws Exception {

    // Given & When
    ProductDto.CheapestProductResponse cheapestProductResponse
        = productService.getCheapestProductForEachCategory();

    // Then
    log.debug("cheapestProductResponse:[{}]",
        objectMapper.writeValueAsString(cheapestProductResponse));
    assertAll(
        () -> assertFalse(cheapestProductResponse.getCheapestProductList().isEmpty()),
        () -> assertEquals(34100, cheapestProductResponse.getTotalPrice()),
        () -> assertEquals("34,100", cheapestProductResponse.getFormattedTotalPrice())
    );
  }

  @Order(2)
  @Transactional
  @DisplayName("getCheapestBrandForTotalPrice"
      + "_단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회")
  @Test
  void testGetCheapestBrandForTotalPrice() throws Exception {

    // Given & When
    ProductDto.CheapestResponse cheapestResponse
        = productService.getCheapestBrandForTotalPrice();

    // Then
    log.debug("cheapestResponse:[{}]",
        objectMapper.writeValueAsString(cheapestResponse));
    assertAll(
        () -> assertEquals("D", cheapestResponse.getCheapestBrandResponse().getBrand()),
        () -> assertFalse(
            cheapestResponse.getCheapestBrandResponse().getCategoryList().isEmpty()),
        () -> assertEquals("36,100",
            cheapestResponse.getCheapestBrandResponse().getFormattedTotalPrice())
    );
  }
}