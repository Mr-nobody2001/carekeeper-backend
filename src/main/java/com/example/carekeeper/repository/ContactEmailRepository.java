package com.example.carekeeper.repository;

import com.example.carekeeper.model.ContactEmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContactEmailRepository extends JpaRepository<ContactEmailEntity, UUID> {
    /**
     * Retorna todos os contatos associados a um usuário.
     */
    List<ContactEmailEntity> findByOwnerId(UUID ownerId);
}
