package com.zestindiait.service;

import com.zestindiait.entites.Product;

import java.util.List;

public interface ProductService {
    Product saveProduct(Product product);

    List<Product> getAllProduct();

    Product getProductById(String id);

    void deleteProduct(String id);
}
