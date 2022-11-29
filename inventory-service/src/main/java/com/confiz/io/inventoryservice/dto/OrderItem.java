package com.confiz.io.inventoryservice.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderItem {
    private  UUID productId;
    private  Integer quantity;
    private  BigDecimal price;
    private  BigDecimal subTotal;
}
