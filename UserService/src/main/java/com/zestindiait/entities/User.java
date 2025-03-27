package com.zestindiait.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@ToString
@Setter
@Getter
public class User {

    @Id
    private String userId;
    private String userName;
    private String userPassword;
    @Enumerated
    private Role role;

}
