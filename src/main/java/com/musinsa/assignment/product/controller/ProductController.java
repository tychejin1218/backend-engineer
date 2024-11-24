package com.musinsa.assignment.product.controller;

import com.musinsa.assignment.product.dto.ProductDto;
import com.musinsa.assignment.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/product")
@RestController
public class ProductController {

  private final ProductService productService;

  /**
   * 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회
   *
   * @return 브랜드와 상품 가격, 총액에 대한 정보에 대한 HTTP 응답
   */
  @GetMapping("/cheapest-per-category")
  public ResponseEntity<ProductDto.CheapestProductResponse> getCheapestProductForEachCategory() {
    return ResponseEntity.ok(productService.getCheapestProductForEachCategory());
  }

  /**
   * 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회
   *
   * @return 브랜드와 카테고리의 상품가격, 총액에 대한 정보에 대한 HTTP 응답
   */
  @GetMapping("/cheapest-total-brand")
  public ResponseEntity<ProductDto.CheapestResponse> getCheapestBrandForTotalPrice() {
    return ResponseEntity.ok(productService.getCheapestBrandForTotalPrice());
  }

  /**
   * 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회
   *
   * @param categoryName 카테고리명
   * @return 카테고리별 상품에 대한 HTTP 응답
   */
  @GetMapping("/product/{categoryName}")
  public ResponseEntity<ProductDto.BrandResponse> getProudctByCategory(
      @PathVariable String categoryName) {
    return ResponseEntity.ok(
        productService.getProudctByCategory(ProductDto.Request.of(categoryName)));
  }

  /**
   * 브랜드 및 상품을 저장
   *
   * @param productRequest 저장할 상품 정보
   * @return 저장된 상품에 대한 HTTP 응답
   */
  @PostMapping
  public ResponseEntity<ProductDto.Response> insertProduct(
      @RequestBody ProductDto.Request productRequest) {
    ProductDto.Response response = productService.insertProduct(productRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  /**
   * 브랜드 및 상품을 수정
   *
   * @param productRequest 수정할 상품 정보
   * @return 수정된 상품에 대한 HTTP 응답
   */
  @PutMapping
  public ResponseEntity<ProductDto.Response> updateProduct(
      @RequestBody ProductDto.Request productRequest) {
    ProductDto.Response response = productService.updateProduct(productRequest);
    return ResponseEntity.ok(response);
  }

  /**
   * 브랜드 및 상품을 삭제
   *
   * @param productRequest 삭제할 상품 정보
   * @return 상품 삭제에 대한 HTTP 응답
   */
  @DeleteMapping
  public ResponseEntity<Void> deleteProduct(
      @RequestBody ProductDto.Request productRequest) {
    productService.deleteProduct(productRequest);
    return ResponseEntity.noContent().build();
  }
}
