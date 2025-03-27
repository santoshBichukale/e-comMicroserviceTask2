package com.zestindiait.entites;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@Setter
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String productId;
    private int quantity;
    private String productName;
    private double price;

}
