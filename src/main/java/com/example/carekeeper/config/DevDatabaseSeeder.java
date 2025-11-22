package com.example.carekeeper.config;

import com.example.carekeeper.model.ConfiguracaoEntity;
import com.example.carekeeper.model.UserConfig;
import com.example.carekeeper.repository.ConfiguracaoRepository;
import com.example.carekeeper.service.detection.config.UserConfigService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DevDatabaseSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DevDatabaseSeeder.class);

    private final ConfiguracaoRepository repo;
    private final UserConfigService userConfigService;
    private final ObjectMapper mapper = new ObjectMapper();

    public DevDatabaseSeeder(ConfiguracaoRepository repo, UserConfigService userConfigService) {
        this.repo = repo;
        this.userConfigService = userConfigService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // seed a default config for a demo user (userId = 1) if not present
        Long demoUserId = 1L;
        if (repo.findByUserId(demoUserId).isEmpty()) {
            try {
                UserConfig defaults = userConfigService.getDefaultConfig();
                String json = mapper.writeValueAsString(defaults);
                ConfiguracaoEntity ent = new ConfiguracaoEntity(demoUserId, json);
                repo.save(ent);
                log.info("DevDatabaseSeeder: persisted default config for userId={}", demoUserId);
            } catch (Exception e) {
                log.error("DevDatabaseSeeder: failed to persist default config", e);
            }
        } else {
            log.info("DevDatabaseSeeder: default config already present for userId={}", demoUserId);
        }
    }
}
