package com.confiz.io.orderservice.events;

import com.confiz.io.orderservice.dto.PaymentDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.UUID;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEvent {
    private UUID eventId;
    private Date date;
    private PaymentDto payment;
    private PaymentStatus paymentStatus;
}
