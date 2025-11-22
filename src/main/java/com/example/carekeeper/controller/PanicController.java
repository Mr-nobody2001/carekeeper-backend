package com.example.carekeeper.controller;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import com.example.carekeeper.enums.EmailTemplate;
import com.example.carekeeper.dto.PanicAlertRequest;
import com.example.carekeeper.model.ContactEmailEntity;
import com.example.carekeeper.service.ContactEmailService;
import com.example.carekeeper.service.email.EmailService;

@RestController
@RequestMapping("/emergencia")
@RequiredArgsConstructor
public class PanicController {

    private final EmailService emailService;
    private final ContactEmailService contactEmailService;

    @Value("${STATIC_MAP_API_KEY}")
    private String staticMapApiKey;

    /**
     * Endpoint responsável por disparar um alerta de emergência.
     *
     * <p>Ao receber uma leitura de pânico (via {@link PanicAlertRequest}), o sistema:
     * <ul>
     *   <li>Busca todos os contatos de e-mail associados ao usuário informado.</li>
     *   <li>Gera o corpo do e-mail com informações como leitura, localização e horário do alerta.</li>
     *   <li>Envia o e-mail para todos os contatos cadastrados do usuário.</li>
     * </ul>
     *
     * <p>Retorna:
     * <ul>
     *   <li>{@code 200 OK} se o alerta for processado e enviado.</li>
     *   <li>{@code 204 No Content} caso o usuário não possua contatos cadastrados.</li>
     * </ul>
     * </p>
    */
    @PostMapping("/alerta")
    public ResponseEntity<Void> triggerPanic(
            @RequestBody PanicAlertRequest request,
            @RequestParam UUID userId
    ) {
        // Busca todos os contatos associados ao usuário
        List<ContactEmailEntity> contatos = contactEmailService.getContactsByUserId(userId);

        if (contatos.isEmpty()) {
            return ResponseEntity.noContent().build(); // Nenhum destinatário
        }

        String timestamp = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                .withZone(ZoneId.of("America/Sao_Paulo"))
                .format(Instant.now());

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("message", request.getLeitura());
        placeholders.put("latitude", String.valueOf(request.getLatitude()));
        placeholders.put("longitude", String.valueOf(request.getLongitude()));
        placeholders.put("STATIC_MAP_API_KEY", staticMapApiKey);
        placeholders.put("timestamp", timestamp);

        // Envia o e-mail para todos os contatos do usuário
        for (ContactEmailEntity contato : contatos) {
            emailService.sendEmail(
                    contato.getEmail(),
                    "Alerta de Emergência",
                    EmailTemplate.PANIC_ALERT,
                    placeholders
            );
        }

        return ResponseEntity.ok().build();
    }
}
