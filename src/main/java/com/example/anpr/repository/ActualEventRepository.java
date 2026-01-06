package com.example.anpr.repository;

import com.example.anpr.entity.ActualEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ActualEventRepository extends JpaRepository<ActualEventEntity, Long> {

    Optional<ActualEventEntity> findFirstByBusNoAndDirectionAndEventTimeBetweenOrderByEventTimeAsc(
        String busNo, String direction, LocalDateTime start, LocalDateTime end);

    Optional<ActualEventEntity> findFirstByBusNoAndDirectionAndEventTimeBetweenOrderByEventTimeDesc(
        String busNo, String direction, LocalDateTime start, LocalDateTime end);
        
    List<ActualEventEntity> findByBusNoAndEventTimeBetweenOrderByEventTimeAsc(
        String busNo, LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(DISTINCT e.busNo) FROM ActualEventEntity e WHERE e.eventTime >= :startOfDay AND e.eventTime < :endOfDay")
    long countDistinctBusesOnDate(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
}
