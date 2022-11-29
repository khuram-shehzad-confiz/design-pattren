package com.confiz.io.orderservice.controller;

import com.confiz.io.orderservice.dto.OrderRequestDto;
import com.confiz.io.orderservice.entities.OrderEntity;
import com.confiz.io.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class OrderController {


    @Autowired
    OrderService orderService;
    @PostMapping("/create")
    public OrderEntity createOrder(@RequestBody OrderRequestDto requestDTO){
        requestDTO.setOrderId(UUID.randomUUID());
        return this.orderService.createOrder(requestDTO);
    }

    @GetMapping("/all")
    public List<OrderEntity> getOrders(){
        return this.orderService.getAll();
    }
}
