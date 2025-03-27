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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserServiceFeignClient userServiceFeignClient;

    @Autowired
    private ProductServiceFeignClient productServiceFeignClient;


    @Override
    public Order placeOrder(Order order) {

        User user = userServiceFeignClient.getUserDetails(order.getUserId());

        if (user == null) {
            throw new OrderNotFoundException("User not found with id: " + order.getUserId());
        }


        for (ProductOrder productOrder : order.getProductOrders()) {
            Product product = productServiceFeignClient.getProductDetails(productOrder.getProductId());

            if (product == null) {
                throw new OrderNotFoundException("Product not found with id: " + productOrder.getProductId());
            }
          productOrder.setProductName(product.getProductName());

            double totalPrice = product.getPrice() * productOrder.getQuantity();

            productOrder.setPrice(totalPrice);

            int newQuantity = product.getStockQuantity() - productOrder.getQuantity();
            if(newQuantity<0){
                throw new OrderNotFoundException("Product out of stock with id: " + productOrder.getProductId());
            }
            product.setStockQuantity(newQuantity);
            productServiceFeignClient.updateProduct(product);
        }

        order.setUserName(user.getUserName());
        order.setOrderDate(new Date());
        order.setUserId(user.getUserId());
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
        Order order=orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        for(ProductOrder productOrder : order .getProductOrders()){
            Product product = productServiceFeignClient.getProductDetails(productOrder.getProductId());
            int restoredQuantity = product.getStockQuantity() + productOrder.getQuantity();
            product.setStockQuantity(restoredQuantity);
            productServiceFeignClient.updateProduct(product);

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
