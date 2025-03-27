package com.zestindiait.entites;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Product {
    @Id
    private String productId;
    private String productName;
    private String description;
    private double price;
    private int stockQuantity;
    private String category;

}
