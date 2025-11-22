package com.example.carekeeper.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "configuracao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true, columnDefinition = "VARCHAR(36)")
    private UUID userId;

    @Column(name = "config_json", columnDefinition = "TEXT")
    private String configJson;

    // Construtor só com userId e configJson
    public ConfiguracaoEntity(UUID userId, String configJson) {
        this.userId = userId;
        this.configJson = configJson;
    }
}
