package com.confiz.io.paymentservice.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private  UUID productId;
    private  Integer quantity;
    private  BigDecimal price;
    private  BigDecimal subTotal;
}
