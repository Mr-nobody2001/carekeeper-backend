package com.example.carekeeper.config;

import com.example.carekeeper.enums.AccidentType;
import com.example.carekeeper.model.AccidentRecordEntity;
import com.example.carekeeper.repository.AccidentRecordRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Random;
import java.util.UUID;

@Configuration
public class AccidentRecordSeeder {

    @Bean
    public CommandLineRunner seedAccidentRecords(AccidentRecordRepository repository) {
        return args -> {
            if (repository.count() > 0) return; // evita duplicação

            Random random = new Random();

            // Limites aproximados de Goiás
            double minLat = -18.0;
            double maxLat = -12.5;
            double minLon = -52.5;
            double maxLon = -46.0;

            // 5 acidentes de hoje
            LocalDate today = LocalDate.now();
            long startOfDay = today.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
            long endOfDay = today.plusDays(1).atStartOfDay().minusNanos(1).toInstant(ZoneOffset.UTC).toEpochMilli();

            for (int i = 0; i < 5; i++) {
                UUID userId = UUID.randomUUID();
                double lat = minLat + (maxLat - minLat) * random.nextDouble();
                double lon = minLon + (maxLon - minLon) * random.nextDouble();

                String sensorJson = String.format(
                        "{\"accelerometerX\": %.2f, \"accelerometerY\": %.2f, \"accelerometerZ\": %.2f, \"latitude\": %.6f, \"longitude\": %.6f}",
                        random.nextDouble() * 10,
                        random.nextDouble() * 10,
                        random.nextDouble() * 10,
                        lat,
                        lon
                );

                AccidentType accidentType = AccidentType.values()[random.nextInt(AccidentType.values().length)];

                long detectedAt = startOfDay + (long) (random.nextDouble() * (endOfDay - startOfDay));

                AccidentRecordEntity record = new AccidentRecordEntity(userId, sensorJson, accidentType, detectedAt);
                repository.save(record);
            }

            // 15 registros nos últimos 3 dias
            for (int i = 0; i < 15; i++) {
                UUID userId = UUID.randomUUID();
                double lat = minLat + (maxLat - minLat) * random.nextDouble();
                double lon = minLon + (maxLon - minLon) * random.nextDouble();

                String sensorJson = String.format(
                        "{\"accelerometerX\": %.2f, \"accelerometerY\": %.2f, \"accelerometerZ\": %.2f, \"latitude\": %.6f, \"longitude\": %.6f}",
                        random.nextDouble() * 10,
                        random.nextDouble() * 10,
                        random.nextDouble() * 10,
                        lat,
                        lon
                );

                AccidentType accidentType = AccidentType.values()[random.nextInt(AccidentType.values().length)];

                long detectedAt = Instant.now().minusSeconds(1 + random.nextInt(3 * 24 * 60 * 60)).toEpochMilli();

                AccidentRecordEntity record = new AccidentRecordEntity(userId, sensorJson, accidentType, detectedAt);
                repository.save(record);
            }

            System.out.println("Seeder: 20 registros de acidentes inseridos (5 hoje) com sucesso!");
        };
    }
}
