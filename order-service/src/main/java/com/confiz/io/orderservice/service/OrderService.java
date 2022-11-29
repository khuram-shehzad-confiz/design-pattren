package com.confiz.io.orderservice.service;

import com.confiz.io.orderservice.dto.OrderRequestDto;
import com.confiz.io.orderservice.entities.OrderEntity;
import com.confiz.io.orderservice.entities.OrderItemEntity;
import com.confiz.io.orderservice.events.OrderStatus;
import com.confiz.io.orderservice.publisher.OrderStatusPublisher;
import com.confiz.io.orderservice.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final Map<Integer, Integer> productPriceMap= new HashMap<>();

    @Autowired
    private OrderRepository  orderRepository;

    @Autowired
    private OrderStatusPublisher statusPublisher;
    @Transactional
    public OrderEntity createOrder(OrderRequestDto orderRequestDTO){
        OrderEntity orderEntity = this.orderRepository.save(this.dtoToEntity(orderRequestDTO));
//        kafkaTemplate.send("order-event", orderEntity.toString());
        this.statusPublisher.raiseOrderEvent(orderEntity, OrderStatus.ORDER_CREATED);
        return orderEntity;
    }

    private OrderEntity dtoToEntity(final OrderRequestDto dto){
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(dto.getOrderId());
        List<OrderItemEntity> itemEntities=  dto.getProducts().stream()
                .map(x -> {
            return OrderItemEntity.builder()
//                    .id(dto.getOrderId())
                    .orderId(dto.getOrderId())
                    .price(x.getPrice())
                    .productId(x.getProductId())
                    .subTotal(x.getSubTotal())
                    .quantity(x.getQuantity()).build();
//                    .id(dto.getOrderId() )
        }).collect(Collectors.toList());
        orderEntity.setItems(itemEntities);
//        orderEntity.setProductId(dto.getProducts().get(0).getQuantity());
        orderEntity.setUserId(dto.getUserId());
        orderEntity.setOrderStatus(OrderStatus.ORDER_CREATED);
        orderEntity.setPrice(dto.getPrice());
        return orderEntity;
    }

    public List<OrderEntity> getAll() {
        return this.orderRepository.findAll();
    }
}
