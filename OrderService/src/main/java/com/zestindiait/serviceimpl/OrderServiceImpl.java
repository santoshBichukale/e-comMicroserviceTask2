package com.zestindiait.serviceimpl;

import com.zestindiait.customexception.OrderNotFoundException;
import com.zestindiait.dto.Product;
import com.zestindiait.dto.User;
import com.zestindiait.entites.Order;
import com.zestindiait.entites.ProductOrder;
import com.zestindiait.externalservice.ProductServiceFeignClient;
import com.zestindiait.externalservice.UserServiceFeignClient;
import com.zestindiait.repository.OrderRepository;
import com.zestindiait.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserServiceFeignClient userServiceFeignClient;

    @Autowired
    private ProductServiceFeignClient productServiceFeignClient;

    @Override
    public Order placeOrder(Order order) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new OrderNotFoundException("Missing or invalid token");
        }
        System.err.println("Authorization Header:" + authHeader);


        Optional<User> user = Optional.ofNullable(userServiceFeignClient.getUserDetails(authHeader));
        if (user.isEmpty()) {
            throw new OrderNotFoundException("User not found");
        }


        for (ProductOrder productOrder : order.getProductOrders()) {

            Product product = productServiceFeignClient.getProductDetails(productOrder.getProductId(), authHeader);

            if (product == null) {
                throw new OrderNotFoundException("Product not found with id: " + productOrder.getProductId());
            }

            productOrder.setProductName(product.getProductName());
            double price = product.getPrice() * productOrder.getQuantity();
            productOrder.setPrice(price);

            int newQuantity = product.getStockQuantity() - productOrder.getQuantity();
            if (newQuantity < 0) {
                throw new OrderNotFoundException("Product out of stock with id: " + productOrder.getProductId());
            }

            product.setStockQuantity(newQuantity);
            productServiceFeignClient.updateStock(product, "SECRET_INTERNAL_KEY");


        }


        order.setUserName(user.get().getUserName());
        order.setOrderDate(new Date());
        order.setUserId(user.get().getUserId());
        order.setTotalAmount(calculateTotalAmount(order.getProductOrders()));

        return orderRepository.save(order);
    }


    @Override
    public Order getOrderById(String id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    }

    @Override
    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

    @Override
    public void deleteOrder(String id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        for (ProductOrder productOrder : order.getProductOrders()) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new OrderNotFoundException("Missing or invalid token");
            }
            Product product = productServiceFeignClient.getProductDetails(productOrder.getProductId(), authHeader);
            int restoredQuantity = product.getStockQuantity() + productOrder.getQuantity();
            product.setStockQuantity(restoredQuantity);
            productServiceFeignClient.updateStock(product, "SECRET_INTERNAL_KEY");



        }
        orderRepository.deleteById(id);
    }

    @Override
    public List<Order> getOrderByUserId(String userId) {
        return orderRepository.findAllByUserId(userId);
    }


    private double calculateTotalAmount(List<ProductOrder> productOrders) {
        double totalAmount = 0;
        for (ProductOrder productOrder : productOrders) {
            totalAmount += productOrder.getPrice();
        }
        return totalAmount;
    }

}
