package com.musinsa.assignment.product.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musinsa.assignment.common.exception.ApiException;
import com.musinsa.assignment.product.dto.ProductDto;
import com.musinsa.assignment.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class ProductServiceTest {

  @Autowired
  ProductService productService;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  ObjectMapper objectMapper;

  @Order(1)
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
        () -> assertEquals(34_100, cheapestProductResponse.getTotalPrice()),
        () -> assertEquals("34,100", cheapestProductResponse.getFormattedTotalPrice())
    );
  }

  @Order(2)
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

  @Order(3)
  @DisplayName("getProudctByCategory"
      + "_카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회")
  @Test
  void testGetProudctByCategory() throws Exception {

    // Given
    ProductDto.Request productRequest = ProductDto.Request.builder()
        .categoryName("상의")
        .build();

    // When
    ProductDto.BrandResponse productBrandResponse = productService.getProudctByCategory(
        productRequest);

    // Then
    log.debug("productBrandResponse:[{}]",
        objectMapper.writeValueAsString(productBrandResponse));
    assertAll(
        () -> assertEquals("상의", productBrandResponse.getCategoryName()),
        () -> assertEquals("C", productBrandResponse.getMinPriceList().get(0).getBrand()),
        () -> assertEquals("I", productBrandResponse.getMaxPriceList().get(0).getBrand())
    );
  }

  @Order(4)
  @DisplayName("insertProduct_브랜드 및 상품을 저장")
  @Test
  void testInsertProduct() throws Exception {

    // Given
    ProductDto.Request productRequest = ProductDto.Request.builder()
        .brand("J")
        .categoryName("상의")
        .price(12_500)
        .build();

    // When
    ProductDto.Response insertResponse = productService.insertProduct(productRequest);

    // Then
    log.debug("insertResponse:[{}]",
        objectMapper.writeValueAsString(insertResponse));
    assertAll(
        () -> assertNotNull(insertResponse.getId()),
        () -> assertEquals("J", insertResponse.getBrand()),
        () -> assertEquals("상의", insertResponse.getCategory()),
        () -> assertEquals(12_500, insertResponse.getPrice())
    );
  }

  @Order(5)
  @DisplayName("updateProduct_브랜드 및 상품 수정를 성공한 경우")
  @Test
  void testUpdateProductSuccess() throws Exception {

    // Given
    ProductDto.Request insertProductRequest = ProductDto.Request.builder()
        .brand("J")
        .categoryName("상의")
        .price(12_500)
        .build();

    ProductDto.Response insertedProductResponse =
        productService.insertProduct(insertProductRequest);

    ProductDto.Request updateProductRequest = ProductDto.Request.builder()
        .id(insertedProductResponse.getId())
        .brand("J")
        .categoryName("아우터")
        .price(15_000)
        .build();

    // When
    ProductDto.Response updatedResponse = productService.updateProduct(updateProductRequest);

    // Then
    log.debug("updatedResponse:[{}]",
        objectMapper.writeValueAsString(updatedResponse));
    assertAll(
        () -> assertNotNull(updatedResponse.getId()),
        () -> assertEquals("J", updatedResponse.getBrand()),
        () -> assertEquals("아우터", updatedResponse.getCategory()),
        () -> assertEquals(15_000, updatedResponse.getPrice())
    );
  }

  @Order(6)
  @DisplayName("updateProduct_브랜드 및 상품이 존재하지 않아 ApiException 발생한 경우")
  @Test
  void testUpdateNonexistentProduct() {

    // Given
    ProductDto.Request productRequest = ProductDto.Request.builder()
        .id(0L)
        .build();

    // When & Then
    assertThrows(ApiException.class, () -> productService.updateProduct(productRequest));
  }

  @Order(7)
  @DisplayName("deleteProduct_브랜드 및 상품을 삭제를 성공한 경우")
  @Test
  void testDeleteProductSuccess() {

    // Given
    ProductDto.Request insertProductRequest = ProductDto.Request.builder()
        .brand("J")
        .categoryName("상의")
        .price(12_500)
        .build();

    ProductDto.Response insertedProductResponse =
        productService.insertProduct(insertProductRequest);

    ProductDto.Request deleteProductRequest = ProductDto.Request.builder()
        .id(insertedProductResponse.getId())
        .build();

    // When
    productService.deleteProduct(deleteProductRequest);

    // Then
    assertFalse(productRepository.findById(deleteProductRequest.getId()).isPresent());
  }

  @Order(8)
  @DisplayName("deleteProduct_브랜드 및 상품이 존재하지 않아 ApiException 발생한 경우")
  @Test
  void testDeleteNonexistentProduct() {

    // Given
    ProductDto.Request deleteProductRequest = ProductDto.Request.builder()
        .id(0L)
        .build();

    // When & Then
    assertThrows(ApiException.class, () -> productService.deleteProduct(deleteProductRequest));
  }
}