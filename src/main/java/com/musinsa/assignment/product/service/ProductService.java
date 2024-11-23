package com.musinsa.assignment.product.service;

import com.musinsa.assignment.product.dto.ProductDto;
import com.musinsa.assignment.product.dto.ProductDto.CheapestResponse;
import com.musinsa.assignment.product.dto.ProductDto.Response;
import com.musinsa.assignment.product.entity.Product;
import com.musinsa.assignment.product.repository.ProductRepository;
import com.musinsa.assignment.type.CategoryType;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

  private final ProductRepository productRepository;
  private final ModelMapper modelMapper;

  /**
   * 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회
   *
   * @return ProductDto.CheapestProductResponse 브랜드와 상품 가격, 총액에 대한 정보
   */
  public ProductDto.CheapestProductResponse getCheapestProductForEachCategory() {

    List<Product> products = productRepository.findAll();

    // 카테고리를 기준으로 가장 저렴한 상품을 조회하여 맵에 저장
    Map<String, Response> categoryMap = new HashMap<>();
    for (Product product : products) {
      ProductDto.Response productResponse = modelMapper.map(product, ProductDto.Response.class);
      String category = productResponse.getCategory();

      // TODO : 스니커즈의 경우 브랜드 A와 G가 가격이 동일함
      // 상품의 가격이 기존의 가장 저렴한 상품의 가격보다 같거나 저렴한지 확인
      if (!categoryMap.containsKey(category) ||
          productResponse.getPrice() <= categoryMap.get(category).getPrice()) {
        categoryMap.put(category, productResponse);
      }
    }

    int totalPrice = 0;
    List<ProductDto.CheapestProduct> cheapestProductList = new ArrayList<>();
    for (ProductDto.Response productResponse : categoryMap.values()) {
      CategoryType categoryType = CategoryType.ofEnumName(productResponse.getCategory());
      // 가장 저렴한 상품을 리스트에 추가
      cheapestProductList.add(
          ProductDto.CheapestProduct.of(
              productResponse.getCategory(),
              categoryType,
              productResponse.getBrand(),
              productResponse.getPrice()
          ));
      totalPrice += productResponse.getPrice();
    }

    // 카테코리 순서를 기준으로 오름차순 정렬
    cheapestProductList.sort(Comparator.comparing(ProductDto.CheapestProduct::getCategoryOrder));

    return ProductDto.CheapestProductResponse.of(cheapestProductList, totalPrice);
  }

  /**
   * 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회
   *
   * @return ProductDto.CheapestBrandResponse 브랜드와 카테고리의 상품가격, 총액에 대한 정보
   */
  public CheapestResponse getCheapestBrandForTotalPrice() {

    List<Product> products = productRepository.findAll();

    // 브랜드를 기준으로 상품 목록을 맵에 저장
    Map<String, List<ProductDto.Response>> brandMap = products.stream()
        .map(product -> modelMapper.map(product, ProductDto.Response.class))
        .collect(Collectors.groupingBy(ProductDto.Response::getBrand));

    String cheapestBrand = "";
    int cheapestTotalPrice = Integer.MAX_VALUE;
    List<ProductDto.Category> categoryList = new ArrayList<>();

    for (Map.Entry<String, List<ProductDto.Response>> entry : brandMap.entrySet()) {

      String brand = entry.getKey();
      List<ProductDto.Response> productList = entry.getValue();

      // 브랜드의 모든 카테고리 상품의 가격
      int totalPrice = productList.stream()
          .mapToInt(ProductDto.Response::getPrice)
          .sum();

      // 모든 카테고리 상품의 가격의 가장 저렴한 카테고리로 상품을 변경
      if (totalPrice < cheapestTotalPrice) {
        cheapestTotalPrice = totalPrice;
        cheapestBrand = brand;

        categoryList = productList.stream()
            .map(product -> {
              CategoryType categoryType = CategoryType.ofEnumName(product.getCategory());
              return ProductDto.Category.of(product.getCategory(), categoryType,
                  product.getPrice());
            })
            .collect(Collectors.toList());
      }
    }

    return CheapestResponse.of(
        ProductDto.CheapestBrandResponse.of(cheapestBrand, categoryList, cheapestTotalPrice));

  }

  // 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회

  // 브랜드 및 상품을 추가 / 업데이트 / 삭제
}
