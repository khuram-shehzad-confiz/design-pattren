package com.confiz.io.orderservice.events;

import com.confiz.io.orderservice.dto.InventoryDto;
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
public class InventoryEvent {

    private  UUID eventId ;
    private  Date date ;
    private InventoryDto inventory;
    private InventoryStatus status;
}
