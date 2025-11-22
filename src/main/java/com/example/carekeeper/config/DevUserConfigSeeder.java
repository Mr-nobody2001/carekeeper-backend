package com.example.carekeeper.config;

import com.example.carekeeper.model.ConfiguracaoEntity;
import com.example.carekeeper.pojo.UserConfig;
import com.example.carekeeper.repository.ConfiguracaoRepository;
import com.example.carekeeper.service.detection.config.UserConfigService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.core.annotation.Order;

import java.util.UUID;

/**
 * Popula o banco de dados com uma configuração padrão de usuário
 * ao iniciar a aplicação (apenas no profile "dev").
 */
@Component
@Order(2)
@Profile("dev")
public class DevUserConfigSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DevUserConfigSeeder.class);

    private final ConfiguracaoRepository repo;
    private final UserConfigService userConfigService;
    private final ObjectMapper mapper = new ObjectMapper();

    public DevUserConfigSeeder(ConfiguracaoRepository repo, UserConfigService userConfigService) {
        this.repo = repo;
        this.userConfigService = userConfigService;
    }

    @Override
    public void run(ApplicationArguments args) {
        UUID demoUserId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        // Se não existir configuração para o usuário demo, cria uma padrão
        if (repo.findByUserId(demoUserId).isEmpty()) {
            try {
                UserConfig defaults = userConfigService.getDefaultConfig();
                String json = mapper.writeValueAsString(defaults);
                ConfiguracaoEntity ent = new ConfiguracaoEntity(demoUserId, json);
                repo.save(ent);
                log.info("Persisted default user config for userId={}", demoUserId);
            } catch (Exception e) {
                log.error("Failed to persist default user config", e);
            }
        } else {
            log.info("Default user config already exists for userId={}", demoUserId);
        }
    }
}
