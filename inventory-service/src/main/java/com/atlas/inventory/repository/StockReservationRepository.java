package com.atlas.inventory.repository;

import com.atlas.inventory.entity.StockReservation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockReservationRepository extends JpaRepository<StockReservation, Long> {

    Optional<StockReservation> findByReservationKey(String reservationKey);
}
