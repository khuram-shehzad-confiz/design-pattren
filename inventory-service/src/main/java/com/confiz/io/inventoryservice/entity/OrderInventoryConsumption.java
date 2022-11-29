package com.confiz.io.inventoryservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@IdClass(OrderConsumptionKey.class)
public class OrderInventoryConsumption {

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID orderId;
    @Id
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID productId;
    private int quantityConsumed;
    private LocalDateTime createdAt;

}
