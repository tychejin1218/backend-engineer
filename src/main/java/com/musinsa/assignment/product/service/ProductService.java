package com.musinsa.assignment.product.service;

import com.musinsa.assignment.common.exception.ApiException;
import com.musinsa.assignment.common.type.ApiStatusType;
import com.musinsa.assignment.common.type.CategoryType;
import com.musinsa.assignment.product.dto.ProductDto;
import com.musinsa.assignment.product.dto.ProductDto.Brand;
import com.musinsa.assignment.product.dto.ProductDto.BrandResponse;
import com.musinsa.assignment.product.dto.ProductDto.Category;
import com.musinsa.assignment.product.dto.ProductDto.CheapestBrandResponse;
import com.musinsa.assignment.product.dto.ProductDto.CheapestProduct;
import com.musinsa.assignment.product.dto.ProductDto.CheapestProductResponse;
import com.musinsa.assignment.product.dto.ProductDto.CheapestResponse;
import com.musinsa.assignment.product.dto.ProductDto.Response;
import com.musinsa.assignment.product.entity.Product;
import com.musinsa.assignment.product.repository.ProductRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
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
  public CheapestProductResponse getCheapestProductForEachCategory() {

    List<Product> productList = productRepository.findAll();
    log.debug("getCheapestProductForEachCategory.productList: {}", productList);

    // 카테고리를 기준으로 가장 저렴한 상품을 조회하여 맵에 저장
    Map<String, Response> categoryMap = new HashMap<>();
    for (Product product : productList) {
      Response productResponse = modelMapper.map(product, Response.class);
      String category = productResponse.getCategory();

      // TODO : 스니커즈의 경우 브랜드 A와 G가 가격이 동일함
      // 상품의 가격이 기존의 가장 저렴한 상품의 가격보다 같거나 저렴한지 확인
      if (!categoryMap.containsKey(category)
          || productResponse.getPrice() <= categoryMap.get(category).getPrice()) {
        categoryMap.put(category, productResponse);
      }
    }

    int totalPrice = 0;
    List<CheapestProduct> cheapestProductList = new ArrayList<>();
    for (Response productResponse : categoryMap.values()) {
      CategoryType categoryType = CategoryType.ofEnumName(productResponse.getCategory());
      // 가장 저렴한 상품을 리스트에 추가
      cheapestProductList.add(
          CheapestProduct.of(
              productResponse.getCategory(),
              categoryType,
              productResponse.getBrand(),
              productResponse.getPrice()
          ));
      totalPrice += productResponse.getPrice();
    }

    // 카테코리 순서를 기준으로 오름차순 정렬
    cheapestProductList.sort(Comparator.comparing(CheapestProduct::getCategoryOrder));

    return CheapestProductResponse.of(cheapestProductList, totalPrice);
  }

  /**
   * 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회
   *
   * @return ProductDto.CheapestBrandResponse 브랜드와 카테고리의 상품가격, 총액에 대한 정보
   */
  public CheapestResponse getCheapestBrandForTotalPrice() {

    List<Product> productList = productRepository.findAll();
    log.debug("getCheapestBrandForTotalPrice.productList: {}", productList);

    // 브랜드를 기준으로 상품 목록을 맵에 저장
    Map<String, List<Response>> brandMap = productList.stream()
        .map(product -> modelMapper.map(product, Response.class))
        .collect(Collectors.groupingBy(Response::getBrand));

    String cheapestBrand = "";
    int cheapestTotalPrice = Integer.MAX_VALUE;
    List<Category> categoryList = new ArrayList<>();

    for (Entry<String, List<Response>> entry : brandMap.entrySet()) {

      String brand = entry.getKey();
      List<Response> tempProductList = entry.getValue();

      // 브랜드의 모든 카테고리 상품의 가격
      int totalPrice = tempProductList.stream()
          .mapToInt(Response::getPrice)
          .sum();

      // 모든 카테고리 상품의 가격의 가장 저렴한 카테고리로 상품을 변경
      if (totalPrice < cheapestTotalPrice) {
        cheapestTotalPrice = totalPrice;
        cheapestBrand = brand;

        categoryList = tempProductList.stream()
            .map(product -> {
              CategoryType categoryType = CategoryType.ofEnumName(product.getCategory());
              return Category.of(product.getCategory(), categoryType,
                  product.getPrice());
            })
            .collect(Collectors.toList());
      }
    }

    return CheapestResponse.of(
        CheapestBrandResponse.of(cheapestBrand, categoryList, cheapestTotalPrice));

  }

  /**
   * 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회
   *
   * @param productRequest 상품 정보 요청
   * @return ProductDto.BrandResponse 최저, 최고 가격 브랜드와 상품 가격 정보
   */
  public ProductDto.BrandResponse getProudctByCategory(ProductDto.Request productRequest) {

    CategoryType categoryType = CategoryType.ofName(productRequest.getCategoryName());
    String category = categoryType.name().toLowerCase(Locale.ROOT);

    List<Product> productList = productRepository.findByCategoryOrderByPriceAsc(category);
    log.debug("getProudctByCategory.productList: {}", productList);

    // 최저, 최고 가격의 상품 설정
    Product minPriceProduct = productList.get(0);
    Product maxPriceProduct = productList.get(productList.size() - 1);

    ProductDto.Brand minPriceBrand = ProductDto.Brand.of(
        minPriceProduct.getBrand(), minPriceProduct.getPrice());
    ProductDto.Brand maxPriceBrand = ProductDto.Brand.of(
        maxPriceProduct.getBrand(), maxPriceProduct.getPrice());

    List<Brand> minPriceList = Collections.singletonList(minPriceBrand);
    List<Brand> maxPriceList = Collections.singletonList(maxPriceBrand);

    return BrandResponse.of(categoryType, minPriceList, maxPriceList);
  }

  /**
   * 브랜드 및 상품을 저장
   *
   * @param productRequest 저장할 상품 정보
   * @return ProductDto.Response 저장된 상품 정보
   */
  public ProductDto.Response insertProduct(ProductDto.Request productRequest) {

    Product insertProduct = modelMapper.map(ProductDto.Request.of(productRequest), Product.class);
    log.debug("insertProduct.product: {}", insertProduct);
    productRepository.save(insertProduct);

    return modelMapper.map(insertProduct, Response.class);
  }

  /**
   * 브랜드 및 상품을 수정
   *
   * @param productRequest 수정할 상품 정보
   * @return ProductDto.Response 수정된 상품 정보
   */
  public ProductDto.Response updateProduct(ProductDto.Request productRequest) {

    Product existingProduct = productRepository.findById(productRequest.getId())
        .orElseThrow(() -> new ApiException(ApiStatusType.BRAND_OR_PRODUCT_NOT_FOUND));
    log.debug("updateProduct.existingProduct : {}", existingProduct);
    existingProduct.setBrand(productRequest.getBrand());
    existingProduct.setCategory(
        CategoryType.ofName(productRequest.getCategoryName()).getName());
    existingProduct.setPrice(productRequest.getPrice());

    return modelMapper.map(existingProduct, Response.class);
  }

  /**
   * 브랜드 및 상품을 삭제
   *
   * @param productRequest 삭제할 상품 정보
   */
  public void deleteProduct(ProductDto.Request productRequest) {

    Product existingProduct = productRepository.findById(productRequest.getId())
        .orElseThrow(() -> new ApiException(ApiStatusType.BRAND_OR_PRODUCT_NOT_FOUND));
    log.debug("deleteProduct.existingProduct : {}", existingProduct);

    productRepository.deleteById(productRequest.getId());
  }
}