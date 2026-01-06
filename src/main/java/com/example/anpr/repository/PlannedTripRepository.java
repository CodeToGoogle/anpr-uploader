package com.example.anpr.repository;

import com.example.anpr.entity.PlannedTripEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlannedTripRepository extends JpaRepository<PlannedTripEntity, Long> {
    List<PlannedTripEntity> findByBusNoOrderByTripNoAsc(String busNo);
    
    @Query("SELECT MAX(p.tripNo) FROM PlannedTripEntity p WHERE p.busNo = :busNo")
    Optional<Integer> findMaxTripNoByBusNo(@Param("busNo") String busNo);
    
    @Query("SELECT DISTINCT p.busNo FROM PlannedTripEntity p")
    List<String> findAllScheduledBusNos();
}
