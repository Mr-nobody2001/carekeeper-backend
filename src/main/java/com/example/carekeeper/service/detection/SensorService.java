package com.example.carekeeper.service.detection;

import com.example.carekeeper.dto.SensorDTO;
import com.example.carekeeper.util.EnvironmentUtil;
import com.example.carekeeper.enums.AccidentType;
import com.example.carekeeper.service.email.EmailService;
import com.example.carekeeper.enums.EmailTemplate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
@Scope("prototype")
public class SensorService {

    private final EmailService emailService;
    private final AccidentDetection accidentDetection;
    private final EnvironmentUtil envUtil;
    private SensorDTO lastReading;
    private boolean hasDetectedAccidents;
    
    @Value("${STATIC_MAP_API_KEY}")
    private String staticMapApiKey;

    private static final Logger logger = Logger.getLogger(SensorService.class.getName());

    public SensorService(EmailService emailService, AccidentDetection accidentDetection, EnvironmentUtil envUtil) {
        this.emailService = emailService;
        this.accidentDetection = accidentDetection;
        this.envUtil = envUtil;
    }

    /**
     * Processa uma leitura do sensor e envia um e-mail de alerta caso algum acidente seja detectado.
     *
     * @param currentReading leitura atual do sensor
     * @return true se algum acidente foi detectado
     */
    public boolean processReading(Long userId, SensorDTO currentReading, boolean isAlertActive) {
        if (isAlertActive || hasDetectedAccidents) {
            return true;
        }

        // Verifica acidentes comparando com a última leitura, usando configuração do usuário
        List<AccidentType> accidents = accidentDetection.check(userId, currentReading, lastReading, envUtil);
        lastReading = currentReading;

        hasDetectedAccidents = hasAccidents(accidents);

        if (hasDetectedAccidents) {
            try {
                // Monta o conteúdo HTML dos alertas
                StringBuilder alertsHtml = new StringBuilder();
                for (AccidentType accident : accidents) {
                    alertsHtml.append("<div class='alert-item'>")
                              .append("<h2>").append(accident.getTitle()).append("</h2>")
                              .append("<p>").append(accident.getDescription()).append("</p>")
                              .append("</div>");
                }

                logger.info("Acidentes detectados: " + accidents.size());

                // Formata data/hora
                String timestamp = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                        .withZone(ZoneId.systemDefault())
                        .format(java.time.Instant.ofEpochMilli(currentReading.getTimestamp()));

                // Mapeia placeholders para o template
                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("message", alertsHtml.toString());
                placeholders.put("timestamp", timestamp);
                placeholders.put("latitude", String.valueOf(currentReading.getLatitude()));
                placeholders.put("longitude", String.valueOf(currentReading.getLongitude()));
                placeholders.put("user", "Gabriel Barbosa"); // pode ser dinâmico
                placeholders.put("STATIC_MAP_API_KEY", staticMapApiKey); 

                String subject = "🚨 " + accidents.size() + " acidente(s) detectado(s)";
                emailService.sendEmail(
                        "gabrielgatinho016@gmail.com",
                        subject,
                        EmailTemplate.EMERGENCY_ALERT_TEMPLATE,
                        placeholders
                );

            } catch (Exception e) {
                logger.severe("Erro ao processar alerta: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return hasDetectedAccidents;
    }

    /**
     * Método auxiliar para verificar se houve algum acidente.
     */
    private boolean hasAccidents(List<AccidentType> accidents) {
        return accidents != null && !accidents.isEmpty();
    }
}
