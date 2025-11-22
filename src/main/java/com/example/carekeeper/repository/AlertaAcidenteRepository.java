package com.example.carekeeper.repository;

import com.example.carekeeper.model.AlertaAcidenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.Optional;

@Repository
public interface AlertaAcidenteRepository extends JpaRepository<AlertaAcidenteEntity, Long> {
    Optional<AlertaAcidenteEntity> findByUserId(UUID userId);
}
