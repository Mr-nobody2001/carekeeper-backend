package com.example.carekeeper.service.detection;

import com.example.carekeeper.dto.SensorDTO;
import com.example.carekeeper.util.EnvironmentUtil;
import com.example.carekeeper.enums.AccidentType;
import com.example.carekeeper.service.email.EmailService;
import com.example.carekeeper.enums.EmailTemplate;
import com.example.carekeeper.model.AccidentRecordEntity;
import com.example.carekeeper.repository.AccidentRecordRepository;
import com.example.carekeeper.service.detection.AccidentDetection;
import com.example.carekeeper.service.email.ContactEmailService;
import com.example.carekeeper.model.ContactEmailEntity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.UUID;

@Service
@Scope("prototype")
public class SensorService {

    private final EmailService emailService;
    private final AccidentDetection accidentDetection;
    private final EnvironmentUtil envUtil;
    private final ContactEmailService contactEmailService; // 🔹 novo
    private final com.example.carekeeper.repository.AccidentRecordRepository accidentRecordRepo;
    private final ObjectMapper objectMapper;

    private SensorDTO lastReading;
    private boolean hasDetectedAccidents;

    @Value("${STATIC_MAP_API_KEY}")
    private String staticMapApiKey;

    private static final Logger logger = Logger.getLogger(SensorService.class.getName());

    public SensorService(
            EmailService emailService,
            AccidentDetection accidentDetection,
            EnvironmentUtil envUtil,
            ContactEmailService contactEmailService,
            com.example.carekeeper.repository.AccidentRecordRepository accidentRecordRepo,
            ObjectMapper objectMapper
    ) {
        this.emailService = emailService;
        this.accidentDetection = accidentDetection;
        this.envUtil = envUtil;
        this.contactEmailService = contactEmailService;
        this.accidentRecordRepo = accidentRecordRepo;
        this.objectMapper = objectMapper;
    }

    /**
     * Processa uma leitura do sensor e envia e-mails de alerta se acidentes forem detectados.
     */
    public boolean processReading(UUID userId, SensorDTO currentReading, boolean isAlertActive) {
        if (isAlertActive || hasDetectedAccidents) {
            return true;
        }

        List<AccidentType> accidents = accidentDetection.check(userId, currentReading, lastReading, envUtil);
        lastReading = currentReading;

        hasDetectedAccidents = hasAccidents(accidents);

        if (hasDetectedAccidents) {
            try {
                // Monta HTML dos alertas detectados
                StringBuilder alertsHtml = new StringBuilder();
                for (AccidentType accident : accidents) {
                    alertsHtml.append("<div class='alert-item'>")
                              .append("<h2>").append(accident.getTitle()).append("</h2>")
                              .append("<p>").append(accident.getDescription()).append("</p>")
                              .append("</div>");
                }

                String timestamp = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                        .withZone(ZoneId.systemDefault())
                        .format(java.time.Instant.ofEpochMilli(currentReading.getTimestamp()));

                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("message", alertsHtml.toString());
                placeholders.put("timestamp", timestamp);
                placeholders.put("latitude", String.valueOf(currentReading.getLatitude()));
                placeholders.put("longitude", String.valueOf(currentReading.getLongitude()));
                placeholders.put("STATIC_MAP_API_KEY", staticMapApiKey);

                // 🔹 Busca contatos do usuário
                List<ContactEmailEntity> contatos = contactEmailService.getContactsByUserId(userId);
                if (contatos.isEmpty()) {
                    logger.warning("Nenhum contato encontrado para o usuário " + userId);
                    return true;
                }

                // Envia para todos os contatos
                String subject = "🚨 " + accidents.size() + " acidente(s) detectado(s)";
                for (ContactEmailEntity contato : contatos) {
                    emailService.sendEmail(
                            contato.getEmail(),
                            subject,
                            EmailTemplate.EMERGENCY_ALERT_TEMPLATE,
                            placeholders
                    );
                }

                // Persiste os registros dos acidentes detectados
                String sensorJson = objectMapper.writeValueAsString(currentReading);
                for (AccidentType at : accidents) {
                    var record = new com.example.carekeeper.model.AccidentRecordEntity(
                            userId, sensorJson, at, currentReading.getTimestamp());
                    accidentRecordRepo.save(record);
                }

                if (envUtil.isDev()) {
                    logger.info("Alertas salvos no banco para userId=" + userId + " (count=" + accidents.size() + ")");
                }

            } catch (Exception e) {
                logger.severe("Erro ao processar alerta de acidente: " + e.getMessage());
            }
        }

        return hasDetectedAccidents;
    }

    private boolean hasAccidents(List<AccidentType> accidents) {
        return accidents != null && !accidents.isEmpty();
    }
}
