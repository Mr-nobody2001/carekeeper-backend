package com.example.carekeeper.repository;

import com.example.carekeeper.model.ConfiguracaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfiguracaoRepository extends JpaRepository<ConfiguracaoEntity, Long> {
    Optional<ConfiguracaoEntity> findByUserId(Long userId);
}
