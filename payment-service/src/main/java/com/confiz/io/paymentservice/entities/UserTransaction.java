package com.confiz.io.paymentservice.entities;

import com.confiz.io.paymentservice.events.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class UserTransaction {

    @Id
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID orderId;
    private int userId;
    private BigDecimal amount;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;

}
