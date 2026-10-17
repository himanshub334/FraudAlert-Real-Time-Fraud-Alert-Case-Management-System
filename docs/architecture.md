# Architecture

```text
Analyst React UI
       |
       v
Spring Boot REST API -----> PostgreSQL (alerts + append-only transition events)
       |  |
       |  +---------------> Redis (demo risk-level cache)
       |
       +------------------> Python FastAPI scoring service

Local transaction HTTP endpoint simulates event ingestion.
AWS SQS and CloudWatch are integration points, not configured in this demo.
```

## Case state transitions
- `OPEN -> INVESTIGATING`
- `INVESTIGATING -> CONFIRMED_FRAUD`
- `INVESTIGATING -> FALSE_POSITIVE`

Every successful transition writes a new audit event; existing audit rows are not updated by application code.
