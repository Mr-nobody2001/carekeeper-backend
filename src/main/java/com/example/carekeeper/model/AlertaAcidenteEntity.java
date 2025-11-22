package com.example.carekeeper.model;

import com.example.carekeeper.enums.AccidentType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "registro_acidente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertaAcidenteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, columnDefinition = "VARCHAR(36)")
    private UUID userId;

    @Column(name = "sensor_json", columnDefinition = "TEXT")
    private String sensorJson;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_acidente", columnDefinition = "VARCHAR(64)")
    private AccidentType tipoAcidente;

    @Column(name = "detected_at")
    private Long detectedAt;

    public AlertaAcidenteEntity(UUID userId, String sensorJson, AccidentType tipoAcidente, Long detectedAt) {
        this.userId = userId;
        this.sensorJson = sensorJson;
        this.tipoAcidente = tipoAcidente;
        this.detectedAt = detectedAt;
    }
}
