package com.example.carekeeper.repository;

import com.example.carekeeper.model.AccidentRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import com.example.carekeeper.dto.AccidentLocationDTO;

@Repository
public interface AccidentRecordRepository extends JpaRepository<AccidentRecordEntity, Long> {

    // Registros de um usuário específico
    List<AccidentRecordEntity> findByUserId(UUID userId);

    // Total de registros de acidentes
    @Query("SELECT COUNT(a) FROM AccidentRecordEntity a")
    Long countTotalRecords();

    // Total de acidentes ocorridos hoje
    @Query("SELECT COUNT(a) FROM AccidentRecordEntity a WHERE a.detectedAt >= :startOfDay AND a.detectedAt <= :endOfDay")
    Long countAccidentsToday(Long startOfDay, Long endOfDay);
}
