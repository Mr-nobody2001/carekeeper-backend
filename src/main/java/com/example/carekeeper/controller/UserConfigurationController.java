package com.example.carekeeper.controller;

import com.example.carekeeper.pojo.UserConfig;
import com.example.carekeeper.service.ConfigurationService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
public class UserConfigurationController {

    private final ConfigurationService configurationService;

    public UserConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    /**
     * Obtém a configuração atual dos sensores de um usuário.
     * Exemplo: GET /api/usuarios/{userId}/sensor-config
     */
    @GetMapping("/{userId}/sensor-config")
    public UserConfig getSensorConfig(@PathVariable UUID userId) {
        return configurationService.getUserConfig(userId);
    }

    /**
     * Atualiza a configuração completa dos sensores de um usuário.
     * Exemplo: PUT /api/usuarios/{userId}/sensor-config
     */
    @PutMapping("/{userId}/sensor-config")
    public UserConfig updateSensorConfig(@PathVariable UUID userId, @RequestBody UserConfig config) {
        return configurationService.updateUserConfig(userId, config);
    }
}
