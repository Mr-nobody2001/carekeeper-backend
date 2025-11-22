package com.example.carekeeper.service.detection.config;

import com.example.carekeeper.model.ConfigurationEntity;
import com.example.carekeeper.pojo.UserConfig;
import java.util.UUID;
import com.example.carekeeper.repository.ConfigurationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class UserConfigService {

    private final ConfigurationRepository ConfigurationRepository;
    private final ObjectMapper mapper;

    public UserConfigService(ConfigurationRepository ConfigurationRepository) {
        this.ConfigurationRepository = ConfigurationRepository;
        this.mapper = new ObjectMapper();
    }

    /**
     * Retorna uma configuração padrão (não persistida). Útil para inicializar detectores padrão.
     */
    public UserConfig getDefaultConfig() {
        return new UserConfig();
    }

    /**
     * Retorna a configuração do usuário desserializada a partir da tabela `configuracao`.
     * Se não houver nada, cria um registro padrão no banco (apenas para ambiente dev) e retorna defaults.
     */
    public UserConfig getConfigForUser(UUID userId) {
        try {
            return ConfigurationRepository.findByUserId(userId)
                    .map(ConfigurationEntity::getConfigJson)
                    .map(json -> {
                        try { return mapper.readValue(json, UserConfig.class); } catch (Exception e) { return new UserConfig(); }
                    })
                    .orElseGet(() -> {
                        // criar configuração padrão e persistir para conveniência no desenvolvimento
                        try {
                            UserConfig defaults = new UserConfig();
                            String json = mapper.writeValueAsString(defaults);
                            ConfigurationEntity ent = new ConfigurationEntity(userId, json);
                            ConfigurationRepository.save(ent);
                            return defaults;
                        } catch (Exception e) {
                            return new UserConfig();
                        }
                    });
        } catch (Exception e) {
            return new UserConfig();
        }
    }
}
