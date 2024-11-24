package com.musinsa.assignment.product.repository;

import com.musinsa.assignment.product.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

  /**
   * 카테고리를 기준으로 상품 목록을 조회 후 가격으로 오름차순 정렬
   *
   * @param category 조회할 카테고리
   * @return List&lt;Product&gt; 상품 목록
   */
  List<Product> findByCategoryOrderByPriceAsc(String category);
}
