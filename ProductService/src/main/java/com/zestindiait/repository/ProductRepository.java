package com.zestindiait.repository;

import com.zestindiait.entites.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,String> {
    Optional<Object> findByProductName(String productName);
}
