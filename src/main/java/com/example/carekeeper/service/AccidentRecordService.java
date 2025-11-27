package com.example.carekeeper.service;

import com.example.carekeeper.dto.AccidentLocationDTO;
import com.example.carekeeper.model.AccidentRecordEntity;
import com.example.carekeeper.repository.AccidentRecordRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.Instant;

/**
 * Serviço responsável por operações de leitura, gravação e estatísticas dos registros de acidentes.
 */
@Service
public class AccidentRecordService {

    private final AccidentRecordRepository accidentRecordRepository;
    private final ObjectMapper objectMapper;

    public AccidentRecordService(AccidentRecordRepository accidentRecordRepository, ObjectMapper objectMapper) {
        this.accidentRecordRepository = accidentRecordRepository;
        this.objectMapper = objectMapper;
    }

    // Operações CRUD básicas
    public List<AccidentRecordEntity> findAll() {
        return accidentRecordRepository.findAll();
    }

    public List<AccidentRecordEntity> findByUserId(UUID userId) {
        return accidentRecordRepository.findByUserId(userId);
    }

    public AccidentRecordEntity save(AccidentRecordEntity record) {
        return accidentRecordRepository.save(record);
    }

    public void deleteById(Long id) {
        accidentRecordRepository.deleteById(id);
    }

    // Novas funções para alimentar os cards
    public Long getTotalRecords() {
        return accidentRecordRepository.countTotalRecords();
    }

    public Long getAccidentsToday() {
        LocalDate today = LocalDate.now();
        long startOfDay = today.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
        long endOfDay = today.plusDays(1).atStartOfDay().minusNanos(1)
                             .toInstant(ZoneOffset.UTC).toEpochMilli();
        return accidentRecordRepository.countAccidentsToday(startOfDay, endOfDay);
    }

    /**
     * Retorna a localização de todos os acidentes.
     */
   public List<AccidentLocationDTO> getAcidentesLocalizacao() {
        return accidentRecordRepository.findAll().stream()
            .map(record -> {
                Double lat = 0.0;
                Double lon = 0.0;

                try {
                    JsonNode json = objectMapper.readTree(record.getSensorJson());
                    if (json.has("latitude")) lat = json.get("latitude").asDouble();
                    if (json.has("longitude")) lon = json.get("longitude").asDouble();
                } catch (Exception e) {
                    // se der erro no JSON, manter 0.0
                }

                return new AccidentLocationDTO(lat, lon, record.getAccidentType(), record.getDetectedAt());
            })
            .collect(Collectors.toList());
    }

    
    /**
     * Retorna array de 12 posições representando acidentes por intervalos de 2 horas.
     */
    public int[] getAcidentesPorHorario() {
        int[] intervals = new int[12]; // cada posição = 2h
        List<Long> timestamps = accidentRecordRepository.findAllTimestamps();

        for (Long ts : timestamps) {
            LocalDateTime dt = LocalDateTime.ofInstant(Instant.ofEpochMilli(ts), ZoneOffset.UTC);
            int hour = dt.getHour();
            int index = hour / 2; // 0-1 => 0, 2-3 => 1, ..., 22-23 => 11
            intervals[index]++;
        }

        return intervals;
    }
}
