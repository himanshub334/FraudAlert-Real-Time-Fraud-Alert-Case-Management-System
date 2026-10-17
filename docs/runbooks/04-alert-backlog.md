# Runbook 04 — Alert queue backlog

**Symptoms:** new transactions are not reflected in the queue promptly.

**Diagnostics**
```bash
curl -s http://localhost:8080/actuator/health
curl -s http://localhost:8080/api/alerts
docker compose logs --tail=150 backend
```

**Likely causes:** API error rate, database latency, or ML service delays. This demo uses synchronous local HTTP ingestion and has no real SQS queue.

**Resolution:** inspect backend and dependency health, restore unhealthy services, then resubmit a synthetic demo transaction.

**Escalation:** application owner if ingestion failures persist.
