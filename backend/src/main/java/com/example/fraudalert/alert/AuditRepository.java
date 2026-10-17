package com.example.fraudalert.alert;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuditRepository extends JpaRepository<AuditEvent, Long> {
    List<AuditEvent> findByAlertIdOrderByOccurredAtAsc(Long alertId);
}
