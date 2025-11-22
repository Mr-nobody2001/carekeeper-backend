package com.example.carekeeper.service;

import com.example.carekeeper.model.AccidentRecordEntity;
import com.example.carekeeper.repository.AccidentRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Serviço responsável por operações de leitura e gravação dos registros de acidentes.
 */
@Service
public class AccidentRecordService {

    private final AccidentRecordRepository accidentRecordRepository;

    public AccidentRecordService(AccidentRecordRepository accidentRecordRepository) {
        this.accidentRecordRepository = accidentRecordRepository;
    }

    /**
     * Retorna todos os registros de acidentes.
     */
    public List<AccidentRecordEntity> findAll() {
        return accidentRecordRepository.findAll();
    }

    /**
     * Retorna todos os registros de acidentes de um usuário específico.
     *
     * @param userId ID do usuário
     */
    public List<AccidentRecordEntity> findByUserId(UUID userId) {
        return accidentRecordRepository.findByUserId(userId);
    }

    /**
     * Salva um novo registro de acidente.
     *
     * @param record entidade a ser salva
     */
    public AccidentRecordEntity save(AccidentRecordEntity record) {
        return accidentRecordRepository.save(record);
    }

    /**
     * Remove um registro pelo ID.
     *
     * @param id identificador do registro
     */
    public void deleteById(Long id) {
        accidentRecordRepository.deleteById(id);
    }
}
