package com.atlas.inventory.service;

import com.atlas.inventory.dto.InventoryItemRequest;
import com.atlas.inventory.dto.StockReservationRequest;
import com.atlas.inventory.entity.InventoryItem;
import com.atlas.inventory.entity.ReservationStatus;
import com.atlas.inventory.entity.StockReservation;
import com.atlas.inventory.exception.ResourceNotFoundException;
import com.atlas.inventory.exception.StockOperationException;
import com.atlas.inventory.repository.InventoryItemRepository;
import com.atlas.inventory.repository.StockReservationRepository;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

    private final InventoryItemRepository inventoryItemRepository;
    private final StockReservationRepository stockReservationRepository;

    public InventoryService(InventoryItemRepository inventoryItemRepository, StockReservationRepository stockReservationRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
        this.stockReservationRepository = stockReservationRepository;
    }

    public List<InventoryItem> findAllItems() {
        return inventoryItemRepository.findAll();
    }

    public InventoryItem findItemById(Long id) {
        return inventoryItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found: " + id));
    }

    @Transactional
    public InventoryItem upsertItem(InventoryItemRequest request) {
        InventoryItem item = inventoryItemRepository.findByProductId(request.productId())
                .orElseGet(InventoryItem::new);
        if (item.getId() == null) {
            item.setProductId(request.productId());
            item.setReservedQuantity(0);
        }
        item.setAvailableQuantity(request.availableQuantity());
        return inventoryItemRepository.save(item);
    }

    @Transactional
    public StockReservation reserveStock(StockReservationRequest request) {
        return stockReservationRepository.findByReservationKey(request.reservationKey())
                .orElseGet(() -> createReservation(request));
    }

    @Transactional
    public StockReservation releaseStock(String reservationKey) {
        StockReservation reservation = stockReservationRepository.findByReservationKey(reservationKey)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found: " + reservationKey));

        if (reservation.getStatus() == ReservationStatus.RELEASED) {
            return reservation;
        }

        if (reservation.getStatus() == ReservationStatus.RESERVED) {
            InventoryItem item = findItemByProductId(reservation.getProductId());
            item.setAvailableQuantity(item.getAvailableQuantity() + reservation.getQuantity());
            item.setReservedQuantity(Math.max(0, item.getReservedQuantity() - reservation.getQuantity()));
            inventoryItemRepository.save(item);
        }

        reservation.setStatus(ReservationStatus.RELEASED);
        return stockReservationRepository.save(reservation);
    }

    private StockReservation createReservation(StockReservationRequest request) {
        InventoryItem item = findItemByProductId(request.productId());
        if (item.getAvailableQuantity() < request.quantity()) {
            StockReservation rejected = new StockReservation();
            rejected.setReservationKey(request.reservationKey());
            rejected.setOrderId(request.orderId());
            rejected.setProductId(request.productId());
            rejected.setQuantity(request.quantity());
            rejected.setStatus(ReservationStatus.REJECTED);
            rejected.setCreatedAt(Instant.now());
            stockReservationRepository.save(rejected);
            throw new StockOperationException("Insufficient stock for product " + request.productId());
        }

        item.setAvailableQuantity(item.getAvailableQuantity() - request.quantity());
        item.setReservedQuantity(item.getReservedQuantity() + request.quantity());
        inventoryItemRepository.save(item);

        StockReservation reservation = new StockReservation();
        reservation.setReservationKey(request.reservationKey());
        reservation.setOrderId(request.orderId());
        reservation.setProductId(request.productId());
        reservation.setQuantity(request.quantity());
        reservation.setStatus(ReservationStatus.RESERVED);
        reservation.setCreatedAt(Instant.now());
        return stockReservationRepository.save(reservation);
    }

    private InventoryItem findItemByProductId(Long productId) {
        return inventoryItemRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found for product: " + productId));
    }
}
