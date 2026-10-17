package com.example.fraudalert.alert;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AlertServiceTest {
    @Mock AlertRepository alerts;
    @Mock AuditRepository audits;
    @Mock StringRedisTemplate redis;
    @Test void duplicateTransactionReturnsExistingAlert() {
        AlertService service = new AlertService(alerts, audits, redis, "http://localhost:1");
        FraudAlert existing = mock(FraudAlert.class);
        when(alerts.findByTransactionId("txn-1")).thenReturn(java.util.Optional.of(existing));
        var req = new TransactionRequest("txn-1", "m-1", new java.math.BigDecimal("25"), "GBP", "RETAIL", "GB", 0, "4111111111111111");
        assertSame(existing, service.ingest(req));
        verify(alerts, never()).save(any());
    }
}
