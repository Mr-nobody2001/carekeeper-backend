package com.example.carekeeper.controller;

import com.example.carekeeper.dto.SensorDTO;
import com.example.carekeeper.service.detection.SensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/monitor")
@RequiredArgsConstructor
public class AccidentDetectionController {

    private final SensorService sensorService;

    /**
     * Endpoint responsável por receber leituras dos sensores de movimento ou impacto,
     * e processá-las para detectar possíveis acidentes.
     *
     * <p>O parâmetro {@code ativo} indica se o alerta manual (ex: botão de pânico)
     * foi acionado pelo usuário.</p>
     *
     * <p>Comportamento:
     * <ul>
     *   <li>Se um acidente for detectado (queda, imobilidade, impacto, etc.), retorna {@code 200 OK}.</li>
     *   <li>Se nenhuma condição de acidente for identificada, retorna {@code 204 No Content}.</li>
     * </ul>
     * </p>
     *
     * @param sensorDTO     Dados da leitura atual do sensor (movimento, aceleração, etc.)
     * @param isAlertActive Indica se o alerta manual está ativo (botão de pânico pressionado)
     * @param userId        Identificador único do usuário que enviou a leitura
     * @return {@code 200 OK} se algum acidente for detectado, ou {@code 204 No Content} caso contrário
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