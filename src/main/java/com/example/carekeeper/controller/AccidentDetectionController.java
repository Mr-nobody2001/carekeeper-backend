package com.example.carekeeper.controller;

import com.example.carekeeper.dto.SensorDTO;
import com.example.carekeeper.service.detection.SensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/monitor")
@RequiredArgsConstructor
public class AccidentDetectionController {

    private final SensorService sensorService;

    /**
     * Recebe uma leitura do sensor e verifica se houve acidente.
     * O parâmetro "ativo" indica se o alerta manual está ativo (ex: botão de pânico).
     * Retorna 200 OK se algum acidente foi detectado, 204 No Content caso contrário.
     */
    @PostMapping("/leitura")
    public ResponseEntity<Void> detectAccident(
            @RequestBody SensorDTO sensorDTO,
            @RequestParam(name = "ativo", defaultValue = "false") boolean isAlertActive,
            @RequestParam(name = "userId") Long userId
    ) {
        boolean accidentDetected = sensorService.processReading(userId, sensorDTO, isAlertActive);

        return accidentDetected
            ? ResponseEntity.ok().build()
            : ResponseEntity.noContent().build();
    }
}