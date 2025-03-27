package com.zestindiait.externalservice;

import com.zestindiait.dto.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("ProductService")
public interface ProductServiceFeignClient {
    @GetMapping("/product/{productId}")
    Product getProductDetails(@PathVariable("productId") String productId);

    @PutMapping("/product")
    Product updateProduct(@RequestBody Product product);
}
