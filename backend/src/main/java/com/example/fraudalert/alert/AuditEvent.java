package com.example.fraudalert.alert;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "fraud_audit_events")
public class AuditEvent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private Long alertId;
    @Column(nullable = false) private String fromStatus;
    @Column(nullable = false) private String toStatus;
    @Column(nullable = false) private String analystId;
    @Column(nullable = false, length = 1000) private String note;
    @Column(nullable = false) private Instant occurredAt = Instant.now();
    protected AuditEvent() {}
    public AuditEvent(Long alertId, String fromStatus, String toStatus, String analystId, String note) {
        this.alertId=alertId; this.fromStatus=fromStatus; this.toStatus=toStatus; this.analystId=analystId; this.note=note;
    }
    public Long getId(){return id;} public Long getAlertId(){return alertId;} public String getFromStatus(){return fromStatus;}
    public String getToStatus(){return toStatus;} public String getAnalystId(){return analystId;} public String getNote(){return note;}
    public Instant getOccurredAt(){return occurredAt;}
}
