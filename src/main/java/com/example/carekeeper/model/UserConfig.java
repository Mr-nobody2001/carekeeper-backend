package com.example.carekeeper.model;

import com.example.carekeeper.enums.Sensitivity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserConfig {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Geofence {
        private boolean enabled = false;
        private double centerLat;
        private double centerLon;
        private double radiusMeters;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Fall {
        private boolean enabled = true;
        private Sensitivity sensitivity = Sensitivity.MEDIUM;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Immobility {
        private boolean enabled = true;
        private long timeLimitMs = 86400L;
    }

    private Geofence geofence = new Geofence();
    private Fall fall = new Fall();
    private Immobility immobility = new Immobility();
}
