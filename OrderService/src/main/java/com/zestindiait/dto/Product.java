package com.zestindiait.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
    private String productId;
    private String productName;
    private String description;
    private double price;
    private int stockQuantity;
    private String category;



}
