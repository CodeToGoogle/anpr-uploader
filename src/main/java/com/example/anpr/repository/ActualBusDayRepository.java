package com.example.anpr.repository;

import com.example.anpr.entity.ActualBusDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ActualBusDayRepository extends JpaRepository<ActualBusDay, Long> {
    Optional<ActualBusDay> findByBusNoAndDate(String busNo, LocalDate date);
}
