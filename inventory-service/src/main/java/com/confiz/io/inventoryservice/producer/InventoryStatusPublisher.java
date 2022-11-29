package com.confiz.io.inventoryservice.producer;

import com.confiz.io.inventoryservice.event.Event;
import com.confiz.io.inventoryservice.event.InventoryEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InventoryStatusPublisher {
    @Value("${kafka.inventory.topic.name}")
    private String inventoryEventTopic;
    @Autowired
    KafkaTemplate<String, Event> kafkaTemplate;

public void raiseInventoryEvent(InventoryEvent inventoryEvent){
    log.info("message to : {} payload: {}", inventoryEventTopic, inventoryEvent.toString());
    kafkaTemplate.send(inventoryEventTopic,inventoryEvent);
}
}
