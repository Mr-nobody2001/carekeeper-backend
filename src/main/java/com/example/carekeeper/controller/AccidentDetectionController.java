package com.example.carekeeper.controller;

import com.example.carekeeper.dto.SensorDTO;
import com.example.carekeeper.service.detection.SensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

/**
 * Controller responsável por gerenciar leituras de sensores e detectar possíveis acidentes.
 * Todos os endpoints desta classe estão sob a rota base "/monitor".
 */
@RestController
@RequestMapping("/monitor")
@RequiredArgsConstructor
public class AccidentDetectionController {

    private final SensorService sensorService;

    /**
     * Endpoint para processar leituras de sensores de movimento ou impacto.
     *
     * Recebe:
     * - sensorDTO: dados do sensor (movimento, aceleração, etc.)
     * - isAlertActive: indica se o alerta manual (botão de pânico) foi acionado
     * - userId: identificador único do usuário que enviou a leitura
     *
     * Comportamento:
     * - Avalia os dados do sensor para detectar acidentes (queda, impacto, imobilidade, etc.)
     * - Considera também se o alerta manual está ativo
     *
     * Retorna:
     * - 200 OK se algum acidente for detectado
     * - 204 No Content se nenhuma condição de acidente for identificada
     *
     * @param sensorDTO     Dados da leitura atual do sensor
     * @param isAlertActive Indica se o alerta manual está ativo
     * @param userId        Identificador do usuário
     * @return {@code 200 OK} se acidente detectado, {@code 204 No Content} caso contrário
     */
    @PostMapping("/leitura")
    public ResponseEntity<Void> detectAccident(
            @RequestBody SensorDTO sensorDTO,
            @RequestParam(name = "ativo", defaultValue = "false") boolean isAlertActive,
            @RequestParam(name = "userId") UUID userId
    ) {
        boolean accidentDetected = sensorService.processReading(userId, sensorDTO, isAlertActive);

        return accidentDetected
            ? ResponseEntity.ok().build()
            : ResponseEntity.noContent().build();
    }
}
