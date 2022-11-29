package com.confiz.io.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    @NotNull
    private  UUID productId;
    @NotNull
    private  Integer quantity;
    @NotNull
    private  BigDecimal price;
    @NotNull
    private  BigDecimal subTotal;
}
