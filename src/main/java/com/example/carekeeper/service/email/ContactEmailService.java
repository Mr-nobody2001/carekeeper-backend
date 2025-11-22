package com.example.carekeeper.service;

import com.example.carekeeper.model.ContactEmailEntity;
import com.example.carekeeper.repository.ContactEmailRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Serviço responsável por operações relacionadas aos e-mails de contato dos usuários.
 */
@Service
public class ContactEmailService {

    private final ContactEmailRepository contactEmailRepository;

    public ContactEmailService(ContactEmailRepository contactEmailRepository) {
        this.contactEmailRepository = contactEmailRepository;
    }

    /**
     * Retorna todos os contatos associados a um usuário.
     *
     * @param userId ID do usuário dono dos contatos.
     * @return Lista de contatos do usuário.
     */
    public List<ContactEmailEntity> getContactsByUserId(UUID userId) {
        return contactEmailRepository.findByOwnerId(userId);
    }

    /**
     * Salva ou atualiza um contato de e-mail.
     *
     * @param contact Entidade de contato a ser salva.
     * @return Contato salvo.
     */
    public ContactEmailEntity save(ContactEmailEntity contact) {
        return contactEmailRepository.save(contact);
    }

    /**
     * Remove um contato pelo ID.
     *
     * @param contactId ID do contato a ser removido.
     */
    public void delete(UUID contactId) {
        contactEmailRepository.deleteById(contactId);
    }
}
