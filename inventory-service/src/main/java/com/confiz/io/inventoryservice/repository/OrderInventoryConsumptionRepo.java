package com.confiz.io.inventoryservice.repository;

import com.confiz.io.inventoryservice.entity.OrderConsumptionKey;
import com.confiz.io.inventoryservice.entity.OrderInventoryConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderInventoryConsumptionRepo extends JpaRepository<OrderInventoryConsumption, OrderConsumptionKey> {
}
