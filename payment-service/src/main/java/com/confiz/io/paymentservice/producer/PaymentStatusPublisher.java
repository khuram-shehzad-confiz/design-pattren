package com.confiz.io.paymentservice.producer;

import com.confiz.io.paymentservice.events.Event;
import com.confiz.io.paymentservice.events.PaymentEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentStatusPublisher {
    @Value("${kafka.payment.topic.name}")
    private String paymentEventTopic;
    @Autowired
    KafkaTemplate<String, Event> kafkaTemplate;

public void raisePaymentEvent(PaymentEvent paymentEvent){
    log.info("message to : {} payload: {}",paymentEventTopic,paymentEvent.toString());
    kafkaTemplate.send(paymentEventTopic,paymentEvent);
}
}
