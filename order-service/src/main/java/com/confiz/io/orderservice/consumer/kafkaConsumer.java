package com.confiz.io.orderservice.consumer;

import com.confiz.io.orderservice.events.InventoryEvent;
import com.confiz.io.orderservice.events.PaymentEvent;
import com.confiz.io.orderservice.repo.OrderRepository;
import com.confiz.io.orderservice.service.OrderStatusEventHandlerService;
import com.confiz.io.orderservice.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class kafkaConsumer {
    @Autowired
    Utils utils;

    @Autowired
    OrderStatusEventHandlerService eventHandlerService;
    @Autowired
    OrderRepository repository;

    @KafkaListener(topics = "#{utils.getPaymentTopic()}", groupId = "reflectoring-group-1")
//    @KafkaListener(groupId = "reflectoring-group-1", topicPartitions = @TopicPartition(topic = "#{utils.getOrderTopic()}", partitionOffsets = {@PartitionOffset(partition = "0", initialOffset = "0")}))
    public void listener(String data) throws JsonProcessingException {
        PaymentEvent paymentEvent = new JsonMapper().readValue(data, PaymentEvent.class);

        eventHandlerService.updateOrder(paymentEvent);

//        paymentService.processPayment(orderEvent);
        log.info(data);
    }
    @KafkaListener(topics = "#{utils.getInventoryTopic()}", groupId = "reflectoring-group-1")
    public void inventoryListener(String data) throws JsonProcessingException {
        InventoryEvent inventoryEvent= new JsonMapper().readValue(data,InventoryEvent.class);
        eventHandlerService.updddateOrer(inventoryEvent);
        log.info(data);
    }
}
