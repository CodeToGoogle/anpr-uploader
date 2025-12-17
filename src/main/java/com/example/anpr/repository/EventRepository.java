package com.example.anpr.repository;

import com.example.anpr.dto.CountDTO;
import com.example.anpr.dto.JunctionCountDTO;
import com.example.anpr.dto.TimeSeriesDTO;
import com.example.anpr.dto.VehicleCountDTO;
import com.example.anpr.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {

    long countByEventTimeBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new com.example.anpr.dto.CountDTO(e.status, COUNT(e)) FROM EventEntity e WHERE e.eventTime BETWEEN :start AND :end GROUP BY e.status")
    List<CountDTO> countByStatus(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(DISTINCT e.plateNumber) FROM EventEntity e WHERE e.eventTime BETWEEN :start AND :end")
    long countDistinctPlateNumberByEventTimeBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT new com.example.anpr.dto.VehicleCountDTO(e.plateNumber, COUNT(e)) FROM EventEntity e WHERE e.eventTime BETWEEN :start AND :end GROUP BY e.plateNumber HAVING COUNT(e) >= :min")
    List<VehicleCountDTO> findRepeatOffenders(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("min") long min);

    @Query(value = "SELECT DATE_FORMAT(e.event_time, '%Y-%m-%d %H:00:00'), COUNT(e.id) FROM events e WHERE e.event_time BETWEEN :start AND :end GROUP BY 1 ORDER BY 1", nativeQuery = true)
    List<Object[]> getHourlyTrend(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = "SELECT DATE(e.event_time), COUNT(e.id) FROM events e WHERE e.event_time BETWEEN :start AND :end GROUP BY 1 ORDER BY 1", nativeQuery = true)
    List<Object[]> getDailyTrend(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT new com.example.anpr.dto.JunctionCountDTO(e.junctionName, COUNT(e)) FROM EventEntity e WHERE e.eventTime BETWEEN :start AND :end GROUP BY e.junctionName ORDER BY COUNT(e) DESC")
    List<JunctionCountDTO> findTopJunctionsByViolations(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT new com.example.anpr.dto.CountDTO(e.cameraName, COUNT(e)) FROM EventEntity e WHERE e.eventTime BETWEEN :start AND :end GROUP BY e.cameraName ORDER BY COUNT(e) DESC")
    List<CountDTO> findTopCamerasByViolations(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT new com.example.anpr.dto.CountDTO(e.color, COUNT(e)) FROM EventEntity e WHERE e.eventTime BETWEEN :start AND :end GROUP BY e.color ORDER BY COUNT(e) DESC")
    List<CountDTO> countByColor(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT new com.example.anpr.dto.CountDTO(CASE " +
            "WHEN e.speed BETWEEN 0 AND 50 THEN '0-50' " +
            "WHEN e.speed BETWEEN 51 AND 80 THEN '51-80' " +
            "WHEN e.speed BETWEEN 81 AND 120 THEN '81-120' " +
            "ELSE '>120' END, COUNT(e)) " +
            "FROM EventEntity e WHERE e.eventTime BETWEEN :start AND :end AND e.speed IS NOT NULL GROUP BY 1")
    List<CountDTO> getSpeedDistribution(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
