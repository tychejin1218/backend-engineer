package com.musinsa.assignment.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.musinsa.assignment.type.CategoryType;
import java.text.NumberFormat;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class ProductDto {

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @ToString
  public static class Request {

    private String brand;
  }

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @ToString
  public static class Response {

    private int id;
    private String brand;
    private String category;
    private int price;
  }

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @ToString
  public static class CheapestProductResponse {

    private List<CheapestProduct> cheapestProductList;
    private int totalPrice;
    private String formattedTotalPrice;

    public static CheapestProductResponse of(
        List<CheapestProduct> cheapestProductList,
        int totalPrice) {
      return CheapestProductResponse.builder()
          .cheapestProductList(cheapestProductList)
          .totalPrice(totalPrice)
          .formattedTotalPrice(NumberFormat.getNumberInstance().format(totalPrice))
          .build();
    }
  }

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @ToString
  public static class CheapestProduct {

    @JsonIgnore
    private String category;

    private String categoryName;

    @JsonIgnore
    private int categoryOrder;

    private String brand;

    @JsonIgnore
    private int price;

    private String formattedPrice;

    public static CheapestProduct of(String category, CategoryType categoryType,
        String brand, int price) {
      return CheapestProduct.builder()
          .category(category)
          .categoryName(categoryType.getName())
          .categoryOrder(categoryType.getOrder())
          .brand(brand)
          .price(price)
          .formattedPrice(NumberFormat.getNumberInstance().format(price))
          .build();
    }
  }

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor(staticName = "of")
  @ToString
  public static class CheapestResponse{

    @JsonProperty("최저가")
    CheapestBrandResponse cheapestBrandResponse;
  }

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @ToString
  public static class CheapestBrandResponse {

    @JsonProperty("브랜드")
    private String brand;

    @JsonProperty("카테고리")
    private List<Category> categoryList;

    @JsonIgnore
    private int totalPrice;

    @JsonProperty("총액")
    private String formattedTotalPrice;

    public static CheapestBrandResponse of(String brand, List<Category> categoryList,
        int totalPrice) {
      return CheapestBrandResponse.builder()
          .brand(brand)
          .categoryList(categoryList)
          .totalPrice(totalPrice)
          .formattedTotalPrice(NumberFormat.getNumberInstance().format(totalPrice))
          .build();
    }
  }

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @ToString
  public static class Category {

    @JsonIgnore
    private String category;

    @JsonProperty("카테고리")
    private String categoryName;

    @JsonIgnore
    private int categoryOrder;

    @JsonIgnore
    private int price;

    @JsonProperty("가격")
    private String formattedPrice;

    public static Category of(String category, CategoryType categoryType, int price) {
      return Category.builder()
          .category(category)
          .categoryName(categoryType.getName())
          .categoryOrder(categoryType.getOrder())
          .price(price)
          .formattedPrice(NumberFormat.getNumberInstance().format(price))
          .build();
    }
  }
}
