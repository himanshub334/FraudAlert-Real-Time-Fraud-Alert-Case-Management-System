# Runbook 02 — PostgreSQL unavailable

**Symptoms:** API returns errors while reading or persisting alerts/audit events.

**Diagnostics**
```bash
docker compose ps postgres
docker compose logs --tail=100 postgres
docker compose exec postgres pg_isready -U fraudalert -d fraudalert
```

**Likely causes:** database container down, invalid credentials, storage exhaustion, or connection limit reached.

**Resolution:** verify `.env`, container health and available disk; restart only after reviewing logs. Do not delete the named volume.

**Escalation:** database/platform owner for persistent connection or storage errors.
