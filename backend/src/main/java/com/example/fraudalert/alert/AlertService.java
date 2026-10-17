package com.example.fraudalert.alert;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import java.util.*;

@Service
public class AlertService {
    private final AlertRepository alerts;
    private final AuditRepository audits;
    private final StringRedisTemplate redis;
    private final RestClient mlClient;

    public AlertService(AlertRepository alerts, AuditRepository audits, StringRedisTemplate redis,
                        @Value("${fraudalert.ml-service-url}") String mlUrl) {
        this.alerts=alerts; this.audits=audits; this.redis=redis;
        this.mlClient=RestClient.builder().baseUrl(mlUrl).build();
    }

    @Transactional
    public FraudAlert ingest(TransactionRequest request) {
        Optional<FraudAlert> existing = alerts.findByTransactionId(request.transactionId());
        if (existing.isPresent()) return existing.get();
        double ruleScore = 0;
        List<String> reasons = new ArrayList<>();
        if (request.priorTransactionsInWindow() >= 5) { ruleScore += 0.45; reasons.add("VELOCITY_THRESHOLD"); }
        if (request.amount().doubleValue() >= 2500) { ruleScore += 0.25; reasons.add("HIGH_AMOUNT"); }
        if (Set.of("CRYPTO", "GIFT_CARDS", "MONEY_TRANSFER").contains(request.category().toUpperCase())) {
            ruleScore += 0.15; reasons.add("HIGH_RISK_CATEGORY");
        }
        if (!Set.of("US", "GB", "CA", "AU").contains(request.country().toUpperCase())) {
            ruleScore += 0.10; reasons.add("GEOGRAPHY_ANOMALY");
        }
        double mlScore = 0.0;
        try {
            Map<?, ?> scored = mlClient.post().uri("/score")
                .body(Map.of("amount", request.amount().doubleValue(), "prior_transactions", request.priorTransactionsInWindow(),
                    "high_risk_category", Set.of("CRYPTO","GIFT_CARDS","MONEY_TRANSFER").contains(request.category().toUpperCase()),
                    "unusual_country", !Set.of("US","GB","CA","AU").contains(request.country().toUpperCase())))
                .retrieve().body(Map.class);
            if (scored != null && scored.get("fraud_probability") instanceof Number n) mlScore = n.doubleValue();
        } catch (Exception ignored) {
            reasons.add("ML_SERVICE_UNAVAILABLE");
        }
        double score = Math.min(1.0, ruleScore * 0.65 + mlScore * 0.35);
        String level = score >= 0.75 ? "HIGH" : score >= 0.45 ? "MEDIUM" : "LOW";
        String reason = reasons.isEmpty() ? "RULES_CLEAR" : String.join(",", reasons);
        FraudAlert alert = alerts.save(new FraudAlert(request, score, level, reason));
        if (score >= 0.45) redis.opsForValue().set("fraudalert:last-alert:" + alert.getId(), level);
        audits.save(new AuditEvent(alert.getId(), "NONE", "OPEN", "SYSTEM", "Alert created: " + reason));
        return alert;
    }

    @Transactional(readOnly = true)
    public List<FraudAlert> list() { return alerts.findAllByOrderByRiskScoreDescCreatedAtDesc(); }

    @Transactional
    public FraudAlert update(Long id, CaseUpdateRequest request) {
        FraudAlert alert = alerts.findById(id).orElseThrow(() -> new NoSuchElementException("Alert not found"));
        AlertStatus old = alert.getStatus();
        if (!valid(old, request.status())) throw new IllegalArgumentException("Invalid case transition: " + old + " -> " + request.status());
        alert.setStatus(request.status());
        audits.save(new AuditEvent(id, old.name(), request.status().name(), request.analystId(), request.note() == null ? "" : request.note()));
        return alert;
    }

    @Transactional(readOnly = true)
    public List<AuditEvent> history(Long id) { return audits.findByAlertIdOrderByOccurredAtAsc(id); }

    private boolean valid(AlertStatus from, AlertStatus to) {
        return (from == AlertStatus.OPEN && to == AlertStatus.INVESTIGATING)
            || (from == AlertStatus.INVESTIGATING && (to == AlertStatus.CONFIRMED_FRAUD || to == AlertStatus.FALSE_POSITIVE));
    }
}
