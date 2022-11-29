package com.confiz.io.paymentservice.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Utils {


    @Value("${kafka.order.topic.name}")
    private String orderTopic;

    public String getOrderTopic() {
        return orderTopic;
    }
}
