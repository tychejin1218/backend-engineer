package com.musinsa.assignment.product.repository;

import com.musinsa.assignment.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
