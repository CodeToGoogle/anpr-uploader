package com.example.anpr.repository;

import com.example.anpr.entity.BusMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusMasterRepository extends JpaRepository<BusMasterEntity, Long> {
    Optional<BusMasterEntity> findByBusNo(String busNo);
    boolean existsByBusNo(String busNo);
}
