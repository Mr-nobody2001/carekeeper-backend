package com.example.carekeeper.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import com.example.carekeeper.dto.PanicAlertRequest;
import com.example.carekeeper.service.PanicAlertService;

@RestController
@RequestMapping("/emergencia")
@RequiredArgsConstructor
public class PanicController {

    private final PanicAlertService panicAlertService;

    @PostMapping("/alerta")
    public ResponseEntity<Void> triggerPanic(
            @RequestBody PanicAlertRequest request,
            @RequestParam UUID userId
    ) {
        boolean sent = panicAlertService.sendPanicAlert(userId, request);
        return sent ? ResponseEntity.ok().build() : ResponseEntity.noContent().build();
    }
}
