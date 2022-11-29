package com.confiz.io.paymentservice.service;

import com.confiz.io.paymentservice.dto.PaymentDto;
import com.confiz.io.paymentservice.entities.UserTransaction;
import com.confiz.io.paymentservice.events.*;
import com.confiz.io.paymentservice.producer.PaymentStatusPublisher;
import com.confiz.io.paymentservice.repo.UserBalanceRepo;
import com.confiz.io.paymentservice.repo.UserTransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    @Autowired
    UserBalanceRepo balanceRepo;

    @Autowired
    UserTransactionRepo transactionRepo;

    @Autowired
    PaymentStatusPublisher paymentStatusPublisher;

    public void processPayment(OrderEvent orderEvent) {

        if (orderEvent.getOrderStatus().name().equals(OrderStatus.ORDER_CREATED.name())) {
            //process new order
            newOrderEvent(orderEvent);
        } else {
            cancelOrderEvent(orderEvent);
        }
    }

    @Transactional
    private void newOrderEvent(OrderEvent orderEvent) {
        PurchaseOrderDto purchaseOrder = orderEvent.getPurchaseOrder();
        var dto = PaymentDto.of(purchaseOrder.getOrderId(), purchaseOrder.getUserId(), purchaseOrder.getPrice());
        paymentStatusPublisher.raisePaymentEvent(
                balanceRepo.findById(purchaseOrder.getUserId())
                .filter(x -> x.getBalance().compareTo(purchaseOrder.getPrice()) > 0)
                .map(ub -> {
                    ub.setBalance(ub.getBalance().subtract(purchaseOrder.getPrice()));
                    ub.setLastUpdate(LocalDateTime.now());
                    balanceRepo.save(ub);
                    this.transactionRepo.save(
                            UserTransaction.of(purchaseOrder.getOrderId(), purchaseOrder.getUserId(), purchaseOrder.getPrice(), OrderStatus.ORDER_CREATED, LocalDateTime.now()));
                    return new PaymentEvent(dto, PaymentStatus.RESERVED);
                })
                .orElse(new PaymentEvent(dto, PaymentStatus.REJECTED))
        );
    }

    @Transactional
    private void cancelOrderEvent(OrderEvent orderEvent) {

        transactionRepo.findById(orderEvent.getPurchaseOrder().getOrderId())
                .ifPresent(ut -> {
//    ut.setAmount(ut.getAmount().add(orderEvent.getPurchaseOrder().getPrice()));
                    transactionRepo.delete(ut);
                    balanceRepo.findById(ut.getUserId())
                            .ifPresent(x -> {
                                        x.setBalance(x.getBalance().add(orderEvent.getPurchaseOrder().getPrice()));
                                        balanceRepo.save(x);
                                    }
                            );
                });
    }
}
