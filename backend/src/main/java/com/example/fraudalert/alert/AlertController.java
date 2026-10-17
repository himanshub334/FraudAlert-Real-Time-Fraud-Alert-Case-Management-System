package com.example.fraudalert.alert;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {
    private final AlertService service;
    public AlertController(AlertService service) { this.service=service; }
    @PostMapping("/transactions") @ResponseStatus(HttpStatus.CREATED)
    public FraudAlert ingest(@Valid @RequestBody TransactionRequest request) { return service.ingest(request); }
    @GetMapping public List<FraudAlert> list() { return service.list(); }
    @PatchMapping("/{id}/status") public FraudAlert update(@PathVariable Long id, @Valid @RequestBody CaseUpdateRequest request) { return service.update(id, request); }
    @GetMapping("/{id}/history") public List<AuditEvent> history(@PathVariable Long id) { return service.history(id); }
    @PostMapping("/demo") @ResponseStatus(HttpStatus.CREATED)
    public FraudAlert demo() {
        return service.ingest(new TransactionRequest("demo-" + System.currentTimeMillis(), "merchant-42",
            new java.math.BigDecimal("2850.00"), "GBP", "GIFT_CARDS", "ZZ", 5, "4111111111111111"));
    }
}
