package com.example.carekeeper.repository;

import com.example.carekeeper.model.AccidentRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.carekeeper.dto.AccidentTypeCountDTO;

import java.util.List;
import java.util.UUID;

import com.example.carekeeper.dto.AccidentLocationDTO;

@Repository
public interface AccidentRecordRepository extends JpaRepository<AccidentRecordEntity, Long> {

    List<AccidentRecordEntity> findByUserId(UUID userId);

    @Query("SELECT COUNT(a) FROM AccidentRecordEntity a")
    Long countTotalRecords();

    @Query("SELECT COUNT(a) FROM AccidentRecordEntity a WHERE a.detectedAt >= :startOfDay AND a.detectedAt <= :endOfDay")
    Long countAccidentsToday(Long startOfDay, Long endOfDay);

    @Query("SELECT a.detectedAt FROM AccidentRecordEntity a")
    List<Long> findAllTimestamps();
}
