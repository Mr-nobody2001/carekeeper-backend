package com.example.carekeeper.service.detection.detector;

import com.example.carekeeper.interfaces.AccidentDetector;
import com.example.carekeeper.enums.AccidentType;
import com.example.carekeeper.dto.SensorDTO;
import com.example.carekeeper.util.EnvironmentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Deque;

public class FallDetector implements AccidentDetector {

    private static final Logger log = LoggerFactory.getLogger(FallDetector.class);

    private static final double FREE_FALL_THRESHOLD = 2.0;
    private static final double IMPACT_THRESHOLD = 5.5;
    private static final double IMMOBILITY_THRESHOLD = 1.2;
    private static final double GYRO_THRESHOLD = 1.5;
    private static final int READING_WINDOW = 50;
    private static final int MIN_HISTORY_READINGS = 5;
    private static final long IMMOBILITY_TIME_MS = 1500;
    private static final long MAX_FALL_IMPACT_INTERVAL_MS = 800;

    private final Deque<SensorDTO> history = new ArrayDeque<>(READING_WINDOW);
    private SensorDTO lastDetectedFall = null;
    private boolean freeFallPhase = false;
    private boolean impactPhase = false;
    private long freeFallTimestamp = 0;
    private long impactTimestamp = 0;
    private final EnvironmentUtil envUtil;

    public FallDetector(EnvironmentUtil envUtil) {
        this.envUtil = envUtil;
    }

    @Override
    public synchronized boolean detect(SensorDTO current, SensorDTO previous) {
        if (previous == null) {
            history.addLast(current);
            return false;
        }

        history.addLast(current);
        if (history.size() > READING_WINDOW) history.pollFirst();
        if (history.size() < MIN_HISTORY_READINGS) return false;

        double ax = current.getAccelerometerX();
        double ay = current.getAccelerometerY();
        double az = current.getAccelerometerZ();
        double magAcc = Math.sqrt(ax*ax + ay*ay + az*az);

        double dax = ax - previous.getAccelerometerX();
        double day = ay - previous.getAccelerometerY();
        double daz = az - previous.getAccelerometerZ();
        double deltaAcc = Math.sqrt(dax*dax + day*day + daz*daz);

        double gx = current.getGyroscopeX();
        double gy = current.getGyroscopeY();
        double gz = current.getGyroscopeZ();
        double magGyro = Math.sqrt(gx*gx + gy*gy + gz*gz);

        long now = current.getTimestamp();

        if (!freeFallPhase && magAcc < FREE_FALL_THRESHOLD) {
            freeFallPhase = true;
            freeFallTimestamp = now;
            if (envUtil.isDev()) log.info("🟡 Início de queda livre detectado.");
            return false;
        }

        if (freeFallPhase && !impactPhase && deltaAcc > IMPACT_THRESHOLD) {
            if (now - freeFallTimestamp <= MAX_FALL_IMPACT_INTERVAL_MS) {
                impactPhase = true;
                impactTimestamp = now;
                if (envUtil.isDev()) log.info("🔴 Impacto detectado após queda livre.");
            }
        }
        freeFallPhase = false;

        if (impactPhase) {
            long timeSinceImpact = now - impactTimestamp;
            if (timeSinceImpact >= IMMOBILITY_TIME_MS) {
                double sum = 0;
                int count = 0;
                for (SensorDTO s : history) {
                    if (s.getTimestamp() >= impactTimestamp) {
                        double sx = s.getAccelerometerX();
                        double sy = s.getAccelerometerY();
                        double sz = s.getAccelerometerZ();
                        double mag = Math.sqrt(sx*sx + sy*sy + sz*sz);
                        sum += Math.abs(mag - 9.8);
                        count++;
                    }
                }
                double avgAccNoGravity = (count>0)? sum/count : 0.0;

                if (avgAccNoGravity < IMMOBILITY_THRESHOLD) {
                    if (lastDetectedFall == null || now - lastDetectedFall.getTimestamp() > IMMOBILITY_TIME_MS) {
                        lastDetectedFall = current;
                        impactPhase = false;
                        if (envUtil.isDev()) log.info("✅ Queda confirmada (imobilidade detectada).");
                        return true;
                    }
                }
                impactPhase = false;
                if (envUtil.isDev()) log.info("🔄 Fim da fase de impacto (sem imobilidade suficiente).");
            }
        }

        if (magGyro > GYRO_THRESHOLD && envUtil.isDev()) {
            log.info("ℹ️ Movimento rotacional detectado: magGyro={}", magGyro);
        }

        return false;
    }

    @Override
    public synchronized AccidentType getType() {
        return AccidentType.FALL;
    }

    public synchronized void reset() {
        history.clear();
        lastDetectedFall = null;
        freeFallPhase = false;
        impactPhase = false;
        freeFallTimestamp = 0;
        impactTimestamp = 0;
        if (envUtil.isDev()) log.info("♻️ Detector de quedas resetado.");
    }
}
