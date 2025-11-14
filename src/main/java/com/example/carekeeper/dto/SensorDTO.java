package com.example.carekeeper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para dados de sensores do celular.
 * Contém acelerômetro, giroscópio, GPS e timestamp.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorDTO {

    // Movimento
    private Double accelerometerX;
    private Double accelerometerY;
    private Double accelerometerZ;
    private Double gyroscopeX;
    private Double gyroscopeY;
    private Double gyroscopeZ;

    // Localização
    private Double latitude;
    private Double longitude;

    // Tempo
    private Long timestamp;

    // Sensores extras
    // private Integer heartRate;         // bpm
    // private Integer oxygen;            // SpO2 %
    // private Double temperature;        // °C
    // private Integer systolicPressure;
    // private Integer diastolicPressure;
    // private Boolean nearbyMovement;    // true se detectou movimento perto do idoso
    // private Double luminosity;         // lux
}
