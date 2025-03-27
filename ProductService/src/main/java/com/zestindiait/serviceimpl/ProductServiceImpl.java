package com.zestindiait.serviceimpl;

import com.zestindiait.customeexception.ProductAlreadyExistsException;
import com.zestindiait.customeexception.ProductNotFoundException;
import com.zestindiait.entites.Product;
import com.zestindiait.service.ProductService;
import com.zestindiait.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;


    @Override
    public Product saveProduct(Product product) {

        if(productRepository.findByProductName(product.getProductName()).isPresent()){
            throw new ProductAlreadyExistsException("Product already exists with name: " + product.getProductName());
        }

        if (product.getProductId() == null || product.getProductId().isEmpty()) {
            product.setProductId(UUID.randomUUID().toString());
        }
        return productRepository.save(product);
    }


    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(String id) throws ProductNotFoundException {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    @Override
    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

}
