package com.confiz.io.inventoryservice.consumer;

import com.confiz.io.inventoryservice.event.OrderEvent;
import com.confiz.io.inventoryservice.service.InventoryService;
import com.confiz.io.inventoryservice.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaConfusmer {


    @Autowired
    InventoryService inventoryService;

    @Autowired
    Utils utils;

    @KafkaListener(topics = "#{utils.getOrderTopic()}", groupId = "inventory-group-1")
//    @KafkaListener(groupId = "reflectoring-group-1", topicPartitions = @TopicPartition(topic = "#{utils.getOrderTopic()}", partitionOffsets = {@PartitionOffset(partition = "0", initialOffset = "0")}))
    public void listener(String data) throws JsonProcessingException {
        OrderEvent orderEvent = new JsonMapper().readValue(data, OrderEvent.class);
        inventoryService.processInventoryEvent(orderEvent);
        log.info(data);
    }
}
