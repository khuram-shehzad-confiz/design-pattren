package com.confiz.io.inventoryservice.event;

import com.confiz.io.inventoryservice.dto.PurchaseOrderDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.UUID;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {
    private  UUID eventId ;
    private  Date date;
    private PurchaseOrderDto purchaseOrder;
    private OrderStatus orderStatus;
}
