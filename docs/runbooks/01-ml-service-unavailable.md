# Runbook 01 — ML service unavailable

**Symptoms:** ML service health check fails; new alerts include `ML_SERVICE_UNAVAILABLE`.

**Diagnostics**
```bash
docker compose ps
docker compose logs --tail=100 ml-service
curl -fsS http://localhost:8001/health
```

**Likely causes:** container stopped, dependency startup failure, invalid model initialization, or network/DNS issue.

**Resolution:** check container logs, restart `docker compose restart ml-service`, and retry health check. Rule-based scoring continues, but treat results as degraded.

**Escalation:** application/platform owner if health does not recover after restart.
