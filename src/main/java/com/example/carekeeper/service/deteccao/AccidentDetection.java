package com.example.carekeeper.service.detection;

import com.example.carekeeper.dto.SensorDTO;
import com.example.carekeeper.enums.AccidentType;
import com.example.carekeeper.interfaces.AccidentDetector;
import com.example.carekeeper.service.detection.detector.FallDetector;
import com.example.carekeeper.service.detection.detector.GeofenceDetector;
import com.example.carekeeper.service.detection.detector.ProlongedImmobilityDetector;
import com.example.carekeeper.util.EnvironmentUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccidentDetection {

    private final List<AccidentDetector> detectors = new ArrayList<>();

    // Constantes de teste exatas fornecidas
    public static final double LATITUDE_TEST = -15.836;
    public static final double LONGITUDE_TEST = -47.912;
    public static final double RADIUS_TEST = 100; // metros
    public static final long IMMOBILITY_LIMIT_MS = 1 * 60 * 1000; // 5 minutos

    public AccidentDetection(EnvironmentUtil envUtil) {
        detectors.add(new FallDetector(envUtil));
        // detectors.add(new ProlongedImmobilityDetector(IMMOBILITY_LIMIT_MS, envUtil));
        detectors.add(new GeofenceDetector(LATITUDE_TEST, LONGITUDE_TEST, RADIUS_TEST, envUtil));
    }

    public List<AccidentType> check(SensorDTO current, SensorDTO previous) {
        List<AccidentType> detectedAccidents = new ArrayList<>();
        for (AccidentDetector detector : detectors) {
            if (detector.detect(current, previous)) {
                detectedAccidents.add(detector.getType());
            }
        }
        return detectedAccidents;
    }
}
