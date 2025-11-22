package com.example.carekeeper.service.detection;

import com.example.carekeeper.dto.SensorDTO;
import com.example.carekeeper.enums.AccidentType;
import com.example.carekeeper.interfaces.AccidentDetector;
import com.example.carekeeper.service.detection.detector.FallDetector;
import com.example.carekeeper.service.detection.detector.GeofenceDetector;
import com.example.carekeeper.service.detection.detector.ProlongedImmobilityDetector;
import com.example.carekeeper.pojo.UserConfig;
import com.example.carekeeper.service.detection.config.UserConfigService;
import com.example.carekeeper.util.EnvironmentUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccidentDetection {

    private final List<AccidentDetector> detectors = new ArrayList<>();
    private final UserConfigService userConfigService;

    public AccidentDetection(EnvironmentUtil envUtil, UserConfigService userConfigService) {
        // Keep default detectors for backward compatibility (fallback global defaults)
        UserConfig defaults = userConfigService.getDefaultConfig();
        detectors.add(new FallDetector(defaults.getFall(), envUtil));
        detectors.add(new GeofenceDetector(defaults.getGeofence(), envUtil));
        this.userConfigService = userConfigService;
    }

    /**
     * Check using a specific user's configuration (reads JSON from mock repo and builds detectors accordingly).
     */
    public List<AccidentType> check(Long userId, SensorDTO current, SensorDTO previous, EnvironmentUtil envUtil) {
        List<AccidentType> detectedAccidents = new ArrayList<>();

        UserConfig cfg = userConfigService.getConfigForUser(userId);

        List<AccidentDetector> userDetectors = new ArrayList<>();

        // Fall
        if (cfg.getFall() != null && cfg.getFall().isEnabled()) {
            userDetectors.add(new FallDetector(cfg.getFall(), envUtil));
        }

        // Immobility
        if (cfg.getImmobility() != null && cfg.getImmobility().isEnabled()) {
            userDetectors.add(new ProlongedImmobilityDetector(cfg.getImmobility(), envUtil));
        }

        // Geofence
        if (cfg.getGeofence() != null && cfg.getGeofence().isEnabled()) {
            userDetectors.add(new GeofenceDetector(cfg.getGeofence(), envUtil));
        }

        for (AccidentDetector detector : userDetectors) {
            if (detector.detect(current, previous)) {
                detectedAccidents.add(detector.getType());
            }
        }

        return detectedAccidents;
    }
}
