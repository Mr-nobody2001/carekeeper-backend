package com.example.carekeeper.config.seed;

import com.example.carekeeper.model.UserEntity;
import com.example.carekeeper.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Popula a tabela "users" com um usuário padrão em ambiente de desenvolvimento.
 */
@Component
@Order(1)
@Profile("dev")
public class DevUserSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DevUserSeeder.class);
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DevUserSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(); // cria o encoder
    }

    @Override
    public void run(ApplicationArguments args) {
        UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        // Só cria se ainda não existir
        if (userRepository.findById(userId).isEmpty()) {
            UserEntity user = new UserEntity();
            user.setId(userId);
            user.setName("Usuário Demo");
            user.setEmail("demo@carekeeper.com");
            user.setPasswordHash(passwordEncoder.encode("senha123")); // gera hash bcrypt
            user.setPhone("+55 11 99999-9999");
            user.setBirthDate(LocalDate.of(1990, 1, 1));
            user.setPhotoUrl("https://example.com/photo.jpg");
            user.setStatus("ACTIVE");
            user.setCreatedAt(OffsetDateTime.now());
            user.setUpdatedAt(OffsetDateTime.now());
            user.setDeletedAt(null);

            userRepository.save(user);
            log.info("DevUserSeeder: usuário padrão criado com sucesso (id={})", userId);
        } else {
            log.info("DevUserSeeder: usuário padrão já existe (id={})", userId);
        }
    }
}
