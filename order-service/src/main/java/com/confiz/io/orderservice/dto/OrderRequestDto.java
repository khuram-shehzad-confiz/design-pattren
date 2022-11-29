package com.confiz.io.orderservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class OrderRequestDto {

    private Integer userId;
    private List<OrderItem> products;
    private BigDecimal price;
    private UUID orderId;

}