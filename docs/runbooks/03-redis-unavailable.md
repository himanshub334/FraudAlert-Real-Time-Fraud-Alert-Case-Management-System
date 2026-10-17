# Runbook 03 — Redis unavailable

**Symptoms:** Redis health indicator is down or risk-level cache writes fail.

**Diagnostics**
```bash
docker compose ps redis
docker compose logs --tail=100 redis
docker compose exec redis redis-cli ping
```

**Likely causes:** Redis container down, port conflict, or connection configuration mismatch.

**Resolution:** confirm `SPRING_DATA_REDIS_HOST=redis` in Compose, restart Redis, then check `PONG`.

**Escalation:** platform owner if repeated restarts do not restore connectivity.
