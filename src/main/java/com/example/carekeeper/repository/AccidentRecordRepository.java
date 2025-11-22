package com.example.carekeeper.repository;

import com.example.carekeeper.model.AccidentRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccidentRecordRepository extends JpaRepository<AccidentRecordEntity, Long> {
    List<AccidentRecordEntity> findByUserId(UUID userId);
}
