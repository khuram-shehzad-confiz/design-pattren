package com.confiz.io.orderservice.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Utils {


    @Value("${kafka.payment.topic.name}")
    private String paymentTopic;

    @Value("${kafka.inventory.topic.name}")
    private String inventoryTopic;

    public String getPaymentTopic() {
        return paymentTopic;
    }

    public String getInventoryTopic() {
        return inventoryTopic;
    }
}
