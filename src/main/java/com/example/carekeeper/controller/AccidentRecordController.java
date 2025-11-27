package com.example.carekeeper.controller;

import com.example.carekeeper.model.AccidentRecordEntity;
import com.example.carekeeper.dto.AccidentLocationDTO;
import com.example.carekeeper.service.AccidentRecordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/registros-acidentes") 
public class AccidentRecordController {

    private final AccidentRecordService service;

    public AccidentRecordController(AccidentRecordService service) {
        this.service = service;
    }


    /**
     * Retorna todos os registros de acidentes.
     * Exemplo: GET /api/registros-acidentes
     */
    @GetMapping
    public List<AccidentRecordEntity> buscarTodos() {
        return service.findAll();
    }

    /**
     * Retorna todos os registros de acidentes de um usuário específico.
     * Exemplo: GET /api/registros-acidentes/usuario/{userId}
     */
    @GetMapping("/usuario/{userId}")
    public List<AccidentRecordEntity> buscarPorUsuario(@PathVariable UUID userId) {
        return service.findByUserId(userId);
    }

    /**
     * Salva um novo registro de acidente.
     * Exemplo: POST /api/registros-acidentes
     */
    @PostMapping
    public AccidentRecordEntity salvar(@RequestBody AccidentRecordEntity record) {
        return service.save(record);
    }

    /**
     * Remove um registro pelo ID.
     * Exemplo: DELETE /api/registros-acidentes/{id}
     */
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deleteById(id);
    }

    // -------------------
    // Endpoints para os cards do dashboard
    // -------------------

    /**
     * Retorna o total de registros de acidentes.
     * Exemplo: GET /api/registros-acidentes/total-registros
     */
    @GetMapping("/total-registros")
    public Long totalRegistros() {
        return service.getTotalRecords();
    }

    /**
     * Retorna o total de acidentes ocorridos hoje.
     * Exemplo: GET /api/registros-acidentes/acidentes-hoje
     */
    @GetMapping("/acidentes-hoje")
    public Long acidentesHoje() {
        return service.getAccidentsToday();
    }

      /**
     * Retorna a localização de todos os acidentes.
     * Exemplo: GET /api/registros-acidentes/localizacao
     */
    @GetMapping("/localizacao")
    public List<AccidentLocationDTO> getAcidentesLocalizacao() {
        return service.getAcidentesLocalizacao();
    }

    /**
     * Busca os dados de acidentes agrupados por horário (intervalos de 2h)
     * Exemplo: GET /api/registros-acidentes/por-horario
    */
    @GetMapping("/por-horario")
    public int[] getAcidentesPorHorario() {
        return service.getAcidentesPorHorario();
    }
}
