package com.musinsa.assignment.product.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musinsa.assignment.product.dto.ProductDto;
import com.musinsa.assignment.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
class ProductControllerTest {

  @Autowired
  ProductService productService;

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  private static final String PRODUCT_URL = "/v1/product";

  @Order(1)
  @DisplayName("getCheapestProductForEachCategory"
      + "_카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회를 성공한 경우_Status:200")
  @Test
  void testGgetCheapestProductForEachCategorySuccess() throws Exception {

    // Given
    String url = PRODUCT_URL + "/cheapest-per-category";

    // When
    ResultActions resultActions = mockMvc.perform(
        get(url)
            .contentType(MediaType.APPLICATION_JSON)
    );

    // Then
    resultActions
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.cheapestProductList").exists())
        .andExpect(jsonPath("$.cheapestProductList").isNotEmpty())
        .andExpect(jsonPath("$.totalPrice").value(34_100))
        .andExpect(jsonPath("$.formattedTotalPrice").value("34,100"))
        .andDo(print());
  }

  @Order(2)
  @DisplayName("getCheapestBrandForTotalPrice"
      + "_단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회를 성공한 경우"
      + "_Status:200")
  @Test
  void testGetCheapestBrandForTotalPriceSuccess() throws Exception {

    // Given
    String url = PRODUCT_URL + "/cheapest-total-brand";

    // When
    ResultActions resultActions = mockMvc.perform(
        get(url)
            .contentType(MediaType.APPLICATION_JSON)
    );

    // Then
    resultActions
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.최저가.카테고리[*].카테고리").exists())
        .andExpect(jsonPath("$.최저가.카테고리[*].카테고리").isNotEmpty())
        .andExpect(jsonPath("$.최저가.브랜드").value("D"))
        .andExpect(jsonPath("$.최저가.총액").value("36,100"))
        .andDo(print());
  }

  @Order(3)
  @DisplayName("getProudctByCategory"
      + "_카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회를 성공한 경우_Status:200")
  @Test
  void testGetProductByCategorySuccess() throws Exception {

    // Given
    String categoryName = "상의";
    String url = PRODUCT_URL + "/product/" + categoryName;

    // When
    ResultActions resultActions = mockMvc.perform(
        get(url)
            .contentType(MediaType.APPLICATION_JSON)
    );

    // Then
    resultActions
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.카테고리").value("상의"))
        .andExpect(jsonPath("$.최저가[*].브랜드").value("C"))
        .andExpect(jsonPath("$.최저가[*].가격").value("10,000"))
        .andExpect(jsonPath("$.최고가[*].브랜드").value("I"))
        .andExpect(jsonPath("$.최고가[*].가격").value("11,400"))
        .andDo(print());
  }

  @Order(4)
  @DisplayName("insertProduct_브랜드 및 상품을 저장을 성공한 경우"
      + "_Status:201")
  @Test
  void testInsertProductSuccess() throws Exception {

    // Given
    ProductDto.Request productRequest = ProductDto.Request.builder()
        .brand("J")
        .categoryName("상의")
        .price(12_500)
        .build();

    // When
    ResultActions resultActions = mockMvc.perform(
        post(PRODUCT_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productRequest))
    );

    // Then
    resultActions
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.brand").value("J"))
        .andExpect(jsonPath("$.category").value("상의"))
        .andExpect(jsonPath("$.price").value(12_500))
        .andDo(print());
  }

  @Order(5)
  @DisplayName("updateProduct_브랜드 및 상품 수정를 성공한 경우_Status:200")
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
    ResultActions resultActions = mockMvc.perform(
        put(PRODUCT_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateProductRequest))
    );

    // Then
    resultActions
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").exists()) // Ensure that id exists and is not null
        .andExpect(jsonPath("$.brand").value("J"))
        .andExpect(jsonPath("$.category").value("아우터"))
        .andExpect(jsonPath("$.price").value(15_000))
        .andDo(print());
  }

  @Order(6)
  @DisplayName("updateProduct_브랜드 및 상품이 존재하지 않아 ApiException 발생한 경우_Status:400")
  @Test
  void testUpdateProductNonexistentProduct() throws Exception {

    // Given
    ProductDto.Request productRequest = ProductDto.Request.builder()
        .id(0L)
        .build();

    // When
    ResultActions resultActions = mockMvc.perform(
        put(PRODUCT_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productRequest))
    );

    // Then
    resultActions
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value("브랜드 및 상품이 존재하지 않습니다."))
        .andExpect(jsonPath("$.status").value("MSS_401"))
        .andDo(print());
  }

  @Order(7)
  @DisplayName("deleteProduct_브랜드 및 상품을 삭제를 성공한 경우_Status:204")
  @Test
  void testDeleteProductSuccess() throws Exception {

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
    ResultActions resultActions = mockMvc.perform(
        delete(PRODUCT_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(deleteProductRequest))
    );

    // Then
    resultActions
        .andExpect(status().isNoContent())
        .andDo(print());
  }

  @Order(8)
  @DisplayName("deleteProduct_브랜드 및 상품이 존재하지 않아 ApiException 발생한 경우_Status:400")
  @Test
  void testDeleteNonexistentProduct() throws Exception {

    // Given
    ProductDto.Request productRequest = ProductDto.Request.builder()
        .id(0L)
        .build();

    String url = "/v1/product";

    // When
    ResultActions resultActions = mockMvc.perform(
        delete(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productRequest))
    );

    // Then
    resultActions
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value("브랜드 및 상품이 존재하지 않습니다."))
        .andExpect(jsonPath("$.status").value("MSS_401"))
        .andDo(print());
  }
}