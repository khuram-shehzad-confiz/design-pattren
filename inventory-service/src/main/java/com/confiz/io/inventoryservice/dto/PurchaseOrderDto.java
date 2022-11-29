package com.confiz.io.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderDto {
    private UUID orderId;
    private List<OrderItem> products;
    private BigDecimal price;
    private Integer userId;

}
