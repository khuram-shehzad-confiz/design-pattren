package com.confiz.io.paymentservice.consumer;

import com.confiz.io.paymentservice.events.Event;
import com.confiz.io.paymentservice.events.OrderEvent;
import com.confiz.io.paymentservice.service.PaymentService;
import com.confiz.io.paymentservice.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaConfusmer {


    @Autowired
    PaymentService paymentService;

    @Autowired
    Utils utils;

    @KafkaListener(topics = "#{utils.getOrderTopic()}", groupId = "payment-group-1")
//    @KafkaListener(groupId = "reflectoring-group-1", topicPartitions = @TopicPartition(topic = "#{utils.getOrderTopic()}", partitionOffsets = {@PartitionOffset(partition = "0", initialOffset = "0")}))
    public void listener(String data) throws JsonProcessingException {
        OrderEvent orderEvent = new JsonMapper().readValue(data, OrderEvent.class);
        paymentService.processPayment(orderEvent);
        log.info(data);
    }
}
