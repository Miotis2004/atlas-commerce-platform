package com.atlas.inventory.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.atlas.inventory.dto.InventoryItemRequest;
import com.atlas.inventory.dto.StockReservationRequest;
import com.atlas.inventory.entity.ReservationStatus;
import com.atlas.inventory.exception.StockOperationException;
import com.atlas.inventory.repository.InventoryItemRepository;
import com.atlas.inventory.repository.StockReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class InventoryServiceTest {

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    private StockReservationRepository stockReservationRepository;

    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        inventoryService = new InventoryService(inventoryItemRepository, stockReservationRepository);
    }

    @Test
    void reserveAndReleaseStockAdjustsQuantities() {
        inventoryService.upsertItem(new InventoryItemRequest(101L, 10));

        var reservation = inventoryService.reserveStock(new StockReservationRequest("res-1", "order-1", 101L, 4));
        var itemAfterReserve = inventoryItemRepository.findByProductId(101L).orElseThrow();

        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.RESERVED);
        assertThat(itemAfterReserve.getAvailableQuantity()).isEqualTo(6);
        assertThat(itemAfterReserve.getReservedQuantity()).isEqualTo(4);

        var released = inventoryService.releaseStock("res-1");
        var itemAfterRelease = inventoryItemRepository.findByProductId(101L).orElseThrow();

        assertThat(released.getStatus()).isEqualTo(ReservationStatus.RELEASED);
        assertThat(itemAfterRelease.getAvailableQuantity()).isEqualTo(10);
        assertThat(itemAfterRelease.getReservedQuantity()).isEqualTo(0);
    }

    @Test
    void reserveStockFailsWhenNotEnoughQuantity() {
        inventoryService.upsertItem(new InventoryItemRequest(202L, 2));

        assertThatThrownBy(() -> inventoryService.reserveStock(new StockReservationRequest("res-2", "order-2", 202L, 5)))
                .isInstanceOf(StockOperationException.class)
                .hasMessageContaining("Insufficient stock");
    }
}
