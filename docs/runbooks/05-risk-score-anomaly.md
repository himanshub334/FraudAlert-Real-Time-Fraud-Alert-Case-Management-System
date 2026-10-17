# Runbook 05 — Unexpected risk-score behavior

**Symptoms:** risk scores are unexpectedly high/low or ML scores are missing.

**Diagnostics**
```bash
curl -s http://localhost:8001/health
curl -s -X POST http://localhost:8001/score -H 'Content-Type: application/json' -d '{"amount":2850,"prior_transactions":5,"high_risk_category":true,"unusual_country":true}'
```

**Likely causes:** synthetic model limitations, changed feature mapping, or rule thresholds.

**Resolution:** verify feature names/types and thresholds; compare the ML response with the rule reasons. Do not use demo scores to block real transactions.

**Escalation:** ML/model owner for persistent drift or unexplained scoring behavior.
