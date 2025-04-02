package com.zestindiait.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zestindiait.entites.Product;

import com.zestindiait.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;


   @PostMapping
    public ResponseEntity<Product> saveProduct(@RequestBody Product product){
       return ResponseEntity.ok(productService.saveProduct(product));
   }

   @GetMapping
    public ResponseEntity<List<Product>> getAllProduct() {
       return ResponseEntity.ok(productService.getAllProduct());
   }

    @GetMapping("/{id}")
     public ResponseEntity<Product> getProductById(@PathVariable String id) {
         return ResponseEntity.ok(productService.getProductById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Product> updateProduct(@RequestBody Product product) {

        return ResponseEntity.ok(productService.updateProduct(product));
    }

    @PutMapping("/internal/update-stock")
    public ResponseEntity<Void> updateStock(@RequestBody Product product, @RequestHeader("Internal-Auth") String internalAuth) {
        if (!"SECRET_INTERNAL_KEY".equals(internalAuth)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        productService.updateProduct(product);
        return ResponseEntity.ok().build();
    }

}
