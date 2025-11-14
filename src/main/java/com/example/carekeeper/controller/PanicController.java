package com.example.carekeeper.controller;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import com.example.carekeeper.enums.EmailTemplate;
import com.example.carekeeper.dto.PanicAlertRequest;
import com.example.carekeeper.service.email.EmailService;

@RestController
@RequestMapping("/emergencia")
@RequiredArgsConstructor
public class PanicController {

    private final EmailService emailService;

    @Value("${STATIC_MAP_API_KEY}")
    private String staticMapApiKey;

    @PostMapping("/alerta")
    public ResponseEntity<Void> triggerPanic(@RequestBody PanicAlertRequest request) {
        String destinatario = "gabrielgatinho016@gmail.com"; 

        String timestamp = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                .withZone(ZoneId.of("America/Sao_Paulo"))
                .format(Instant.now());

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("message", request.getLeitura());
        placeholders.put("latitude", String.valueOf(request.getLatitude()));
        placeholders.put("longitude", String.valueOf(request.getLongitude()));
        placeholders.put("STATIC_MAP_API_KEY", staticMapApiKey);
        placeholders.put("timestamp", timestamp); 

        emailService.sendEmail(
                destinatario,
                "Alerta de Emergência",
                EmailTemplate.PANIC_ALERT,
                placeholders
        );

        return ResponseEntity.ok().build();
    }
}
