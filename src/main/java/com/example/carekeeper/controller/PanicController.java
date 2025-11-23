package com.example.carekeeper.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import com.example.carekeeper.dto.PanicAlertRequest;
import com.example.carekeeper.service.PanicAlertService;

/**
 * Controller responsável por gerenciar alertas de pânico.
 * Todos os endpoints desta classe estão sob a rota base "/emergencia".
 */
@RestController
@RequestMapping("/emergencia")
@RequiredArgsConstructor
public class PanicController {

    private final PanicAlertService panicAlertService;

    /**
     * Endpoint para disparar um alerta de pânico.
     *
     * Recebe:
     * - request: JSON com os detalhes da leitura de pânico (ex.: localização, mensagem)
     * - userId: identificador do usuário que acionou o alerta
     *
     * Processa:
     * - Envia alerta via serviço de e-mail ou notificações para os contatos cadastrados
     *
     * Retorna:
     * - 200 OK se o alerta foi processado e enviado com sucesso
     * - 204 No Content se o usuário não possui contatos cadastrados
     */
    @PostMapping("/alerta")
    public ResponseEntity<Void> triggerPanic(
            @RequestBody PanicAlertRequest request,
            @RequestParam UUID userId
    ) {
        boolean sent = panicAlertService.sendPanicAlert(userId, request);

        // Retorna OK se enviado, No Content caso não haja destinatários
        return sent ? ResponseEntity.ok().build() : ResponseEntity.noContent().build();
    }
}
