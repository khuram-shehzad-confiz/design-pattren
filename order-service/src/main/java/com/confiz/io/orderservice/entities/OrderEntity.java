package com.confiz.io.orderservice.entities;

import com.confiz.io.orderservice.events.InventoryStatus;
import com.confiz.io.orderservice.events.OrderStatus;
import com.confiz.io.orderservice.events.PaymentStatus;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
@Entity
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class OrderEntity {

    @Id
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;
    private Integer userId;
    @OneToMany(mappedBy = "orderId", cascade = CascadeType.ALL)
    private List<OrderItemEntity> items;
    private BigDecimal price;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private InventoryStatus inventoryStatus;

    @Version
    private int version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntity that = (OrderEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}