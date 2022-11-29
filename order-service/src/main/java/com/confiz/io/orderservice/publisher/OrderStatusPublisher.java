package com.confiz.io.orderservice.publisher;

import com.confiz.io.orderservice.dto.OrderItem;
import com.confiz.io.orderservice.dto.PurchaseOrderDto;
import com.confiz.io.orderservice.entities.OrderEntity;
import com.confiz.io.orderservice.events.Event;
import com.confiz.io.orderservice.events.OrderEvent;
import com.confiz.io.orderservice.events.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class OrderStatusPublisher {
    @Autowired
    KafkaTemplate<String, Event> kafkaTemplate;

    @Value("${kafka.order.topic.name}")
    private String orderEventTopic;

    public void raiseOrderEvent(final OrderEntity orderEntity, OrderStatus orderStatus) {

        List<OrderItem> orderItems = orderEntity.getItems().stream().map(x -> {
            return OrderItem.builder()
                    .productId(x.getProductId())
                    .quantity(x.getQuantity())
                    .price(x.getPrice())
                    .subTotal(x.getSubTotal()).build();
        }).collect(Collectors.toList());
        PurchaseOrderDto orderDto = PurchaseOrderDto.of(orderEntity.getId(), orderItems, orderEntity.getPrice(), orderEntity.getUserId());
        Event orderEvent = new OrderEvent(orderDto, orderStatus);

        log.info("message to : {} payload: {}", orderEventTopic, orderEvent.toString());
//        kafkaTemplate.send(orderEventTopic, orderEvent);
        sendMessageWithCallback(orderEvent);
    }

    public void sendMessageWithCallback(Event orderEvent) {
        ListenableFuture<SendResult<String, Event>> future =
                kafkaTemplate.send(orderEventTopic, orderEvent);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Event>>() {
            @Override
            public void onSuccess(SendResult<String, Event> result) {
                log.info("Message [{}] delivered with offset {}",
                        result.getProducerRecord().value().getEventId(),
                        result.getRecordMetadata().offset());
            }


            @Override
            public void onFailure(Throwable ex) {
                log.warn("Unable to deliver Event [{}]. {}",
                        orderEvent.getEventId(),
                        ex.getMessage());
            }
        });
    }
}
