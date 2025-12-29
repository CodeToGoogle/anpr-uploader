package com.example.anpr.repository;

import com.example.anpr.dto.CountDTO;
import com.example.anpr.entity.OutsheddingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OutsheddingRepository extends JpaRepository<OutsheddingEntity, Long> {

    List<OutsheddingEntity> findByDate(LocalDate date);

    long countByDate(LocalDate date);

    List<OutsheddingEntity> findByBusNoOrderByDateDesc(String busNo);

    @Query("SELECT new com.example.anpr.dto.CountDTO(o.morningRoute, COUNT(o)) FROM OutsheddingEntity o WHERE o.morningRoute IS NOT NULL GROUP BY o.morningRoute ORDER BY COUNT(o) DESC")
    List<CountDTO> findMostFrequentMorningRoutes();

    @Query("SELECT new com.example.anpr.dto.CountDTO(o.eveningRoute, COUNT(o)) FROM OutsheddingEntity o WHERE o.eveningRoute IS NOT NULL GROUP BY o.eveningRoute ORDER BY COUNT(o) DESC")
    List<CountDTO> findMostFrequentEveningRoutes();

    @Query("SELECT b.busNo FROM BusMasterEntity b WHERE b.busNo NOT IN (SELECT o.busNo FROM OutsheddingEntity o WHERE o.date = :date)")
    List<String> findMissingBuses(@Param("date") LocalDate date);

    @Query("SELECT COUNT(o) FROM OutsheddingEntity o WHERE o.date = :date AND o.outTime IS NOT NULL AND o.eveningRoute IS NULL")
    long countMorningOnly(@Param("date") LocalDate date);

    @Query("SELECT COUNT(o) FROM OutsheddingEntity o WHERE o.date = :date AND o.outTime IS NULL AND o.eveningRoute IS NOT NULL")
    long countEveningOnly(@Param("date") LocalDate date);

    @Query("SELECT COUNT(o) FROM OutsheddingEntity o WHERE o.date = :date AND o.outTime IS NOT NULL AND o.eveningRoute IS NOT NULL")
    long countBothShifts(@Param("date") LocalDate date);

    @Query(value = "SELECT b.bus_no as busNo, COUNT(d.date) as absentCount " +
            "FROM bus_master b " +
            "CROSS JOIN (SELECT DISTINCT date FROM outshedding WHERE date >= :startDate) d " +
            "LEFT JOIN outshedding o ON b.bus_no = o.bus_no AND d.date = o.date " +
            "WHERE o.id IS NULL " +
            "GROUP BY b.bus_no " +
            "ORDER BY absentCount DESC", nativeQuery = true)
    List<Object[]> findFrequentAbsenteesRaw(@Param("startDate") LocalDate startDate);
}
