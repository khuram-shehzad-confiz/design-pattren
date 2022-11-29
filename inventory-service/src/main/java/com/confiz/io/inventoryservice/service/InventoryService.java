package com.confiz.io.inventoryservice.service;

import com.confiz.io.inventoryservice.dto.InventoryDto;
import com.confiz.io.inventoryservice.dto.PurchaseOrderDto;
import com.confiz.io.inventoryservice.entity.OrderConsumptionKey;
import com.confiz.io.inventoryservice.entity.OrderInventory;
import com.confiz.io.inventoryservice.entity.OrderInventoryConsumption;
import com.confiz.io.inventoryservice.event.InventoryEvent;
import com.confiz.io.inventoryservice.event.InventoryStatus;
import com.confiz.io.inventoryservice.event.OrderEvent;
import com.confiz.io.inventoryservice.event.OrderStatus;
import com.confiz.io.inventoryservice.producer.InventoryStatusPublisher;
import com.confiz.io.inventoryservice.repository.OrderInventoryConsumptionRepo;
import com.confiz.io.inventoryservice.repository.OrderInventoryRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class InventoryService {


    @Autowired
    private OrderInventoryRepo inventoryRepository;
    @Autowired
    private OrderInventoryConsumptionRepo consumptionRepository;
    @Autowired
    private InventoryStatusPublisher publisher;

    public void processInventoryEvent(OrderEvent orderEvent) {
        if (orderEvent.getOrderStatus().name().equals(OrderStatus.ORDER_CREATED.name())) {
            //process new order
            newOrderEvent(orderEvent);
        } else {
            cancelOrderEvent(orderEvent);
        }
    }

    @Transactional
    private void newOrderEvent(OrderEvent orderEvent) {
        List<String> failureMessages = new ArrayList<>();
        PurchaseOrderDto orderDto = orderEvent.getPurchaseOrder();
        failureMessages = validateProducts(orderDto);
        publisher.raiseInventoryEvent(
                new InventoryEvent(InventoryDto.builder()
                        .orderId(orderDto.getOrderId())
                        .productId(orderDto.getProducts())
                        .build(), failureMessages.isEmpty() ? InventoryStatus.RESERVED : InventoryStatus.REJECTED)
        );


    }

    public List<String> validateProducts(PurchaseOrderDto orderDto) {
        List<String> failureMessages = new ArrayList<>();
        List<OrderInventoryConsumption> inventoryConsumptions = new ArrayList<>();
        orderDto.getProducts().forEach(x -> {
            log.info("productID: {}", x.getProductId());
            Optional<OrderInventory> item = inventoryRepository.findById(x.getProductId());
            if (item.isPresent()) {
                OrderInventory inventory = item.get();
                if (inventory.getAvailableInventory() < x.getQuantity()) {
                    failureMessages.add("Product with id: " + x.getProductId() + "has less quantity");
                    return;
                }
                inventory.setAvailableInventory(inventory.getAvailableInventory() - x.getQuantity());
                inventory.setLastUpdate(LocalDateTime.now());
                inventoryRepository.save(inventory);
                OrderInventoryConsumption inventoryConsumption = OrderInventoryConsumption.builder()
                        .orderId(orderDto.getOrderId())
                        .productId(x.getProductId())
                        .quantityConsumed(x.getQuantity())
                        .createdAt(LocalDateTime.now()).build();
//                inventoryConsumptions.add(inventoryConsumption);
                consumptionRepository.save(inventoryConsumption);
            } else {
                failureMessages.add("Product with id: " + x.getProductId() + " is not available");
            }

        });
//        if (failureMessages.isEmpty()) {
//            consumptionRepository.saveAll(inventoryConsumptions);
//        }
        return failureMessages;
    }

    @Transactional
    private void cancelOrderEvent(OrderEvent orderEvent) {
        orderEvent.getPurchaseOrder().getProducts().forEach(x -> {
            consumptionRepository.findById(new OrderConsumptionKey(orderEvent.getPurchaseOrder().getOrderId(),x.getProductId()))
                    .ifPresent(ci -> {
                        inventoryRepository.findById(x.getProductId())
                                .ifPresent(i -> {
                                            i.setAvailableInventory(i.getAvailableInventory() + ci.getQuantityConsumed());
                                            inventoryRepository.save(i);
                                        }
                                );
                        consumptionRepository.delete(ci);
                    });

        });
    }


}
