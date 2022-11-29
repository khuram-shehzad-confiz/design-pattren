package com.confiz.io.orderservice.service;

import com.confiz.io.orderservice.entities.OrderEntity;
import com.confiz.io.orderservice.events.*;
import com.confiz.io.orderservice.publisher.OrderStatusPublisher;
import com.confiz.io.orderservice.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

@Service
public class OrderStatusEventHandlerService {

    @Autowired
    OrderRepository repository;

    @Autowired
    OrderStatusPublisher publisher;
    @Transactional
    public void updateOrder(PaymentEvent paymentEvent) {
        repository.findById(paymentEvent.getPayment().getOrderId())
                .ifPresent(x -> {
                    x.setPaymentStatus(paymentEvent.getPaymentStatus());
                    repository.save(x);
                    updateOrder(x);
                });
    }

    @Transactional
    public void updddateOrer(InventoryEvent inventoryEvent){
        repository.findById(inventoryEvent.getInventory().getOrderId())
                .ifPresent(x -> {
                    x.setInventoryStatus(inventoryEvent.getStatus());
                    repository.save(x);
                    updateOrder(x);
                });
    }

    public void updateOrder(OrderEntity purchaseOrder) {
        if (Objects.isNull(purchaseOrder.getInventoryStatus()) || Objects.isNull(purchaseOrder.getPaymentStatus()))
            return;
        var isComplete = PaymentStatus.RESERVED.equals(purchaseOrder.getPaymentStatus()) && InventoryStatus.RESERVED.equals(purchaseOrder.getInventoryStatus());
        var orderStatus = isComplete ? OrderStatus.ORDER_COMPLETED : OrderStatus.ORDER_CANCELLED;
        purchaseOrder.setOrderStatus(orderStatus);
        if (!isComplete) {
            this.publisher.raiseOrderEvent(purchaseOrder, orderStatus);
        }
    }
}
