package com.confiz.io.paymentservice.events;

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
