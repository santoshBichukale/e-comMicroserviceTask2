package com.zestindiait.service;

import com.zestindiait.entites.Order;

import java.util.List;

public interface OrderService  {
    Order placeOrder(Order order);

    Order getOrderById(String id);

    List<Order> getAllOrder();

    void deleteOrder(String id);

    List<Order> getOrderByUserId(String userId);

}
