package com.zestindiait.entites;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "ordertable")
public class Order {

    @Id
    private String orderID;
    private String userId;
    private String userName;

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductOrder> productOrders;

    @Temporal(TemporalType.DATE)
    private Date orderDate;
    private double totalAmount;




    @PrePersist
    public void prePersist() {
        if (this.orderID == null) {
            this.orderID = UUID.randomUUID().toString();
        }
    }


}
