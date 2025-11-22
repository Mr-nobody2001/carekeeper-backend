package com.example.carekeeper.service.detection.config;

import com.example.carekeeper.model.ConfiguracaoEntity;
import com.example.carekeeper.model.UserConfig;
import com.example.carekeeper.repository.ConfiguracaoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class UserConfigService {

    private final ConfiguracaoRepository repo;
    private final ObjectMapper mapper;

    public UserConfigService(ConfiguracaoRepository repo) {
        this.repo = repo;
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
    public UserConfig getConfigForUser(Long userId) {
        try {
            return repo.findByUserId(userId)
                    .map(ConfiguracaoEntity::getConfigJson)
                    .map(json -> {
                        try { return mapper.readValue(json, UserConfig.class); } catch (Exception e) { return new UserConfig(); }
                    })
                    .orElseGet(() -> {
                        // criar configuração padrão e persistir para conveniência no desenvolvimento
                        try {
                            UserConfig defaults = new UserConfig();
                            String json = mapper.writeValueAsString(defaults);
                            ConfiguracaoEntity ent = new ConfiguracaoEntity(userId, json);
                            repo.save(ent);
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
