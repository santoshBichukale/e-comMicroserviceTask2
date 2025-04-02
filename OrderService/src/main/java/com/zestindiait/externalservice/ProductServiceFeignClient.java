package com.zestindiait.externalservice;

import com.zestindiait.dto.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("ProductService")
public interface ProductServiceFeignClient {
    @GetMapping("/product/{productId}")
    Product getProductDetails(
            @PathVariable("productId") String productId,
            @RequestHeader("Authorization") String token
    );


    @PutMapping("/product/internal/update-stock")
    void updateStock(@RequestBody Product product, @RequestHeader("Internal-Auth") String internalAuth);


}
