package com.confiz.io.inventoryservice.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class InventoryDto {

    private UUID orderId;
    private List<OrderItem> productId;

}
