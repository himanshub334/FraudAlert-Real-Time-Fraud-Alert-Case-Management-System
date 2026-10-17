# FraudAlert — Real-Time Fraud Alert & Case Management System

Portfolio/demo implementation of an event-driven fraud alert pipeline with configurable rules, an ML scoring microservice, analyst case management, audit history, health checks, and operational runbooks.

## Stack
- Java 17, Spring Boot 3, Spring Data JPA, Spring Boot Actuator
- Python, FastAPI, scikit-learn
- PostgreSQL, Redis
- React, TypeScript, Vite
- Docker Compose, JUnit 5, Mockito, GitHub Actions

## Features
- REST endpoint for transaction ingestion and configurable velocity/amount/category/geography rules
- Python ML scoring API with a deterministic demo model and probability output
- Fraud alert queue sorted by risk score
- Case workflow: `OPEN -> INVESTIGATING -> CONFIRMED_FRAUD | FALSE_POSITIVE`
- Append-only case transition audit events
- Masked PAN display and log-safe transaction summaries
- Health endpoint with database, Redis, and ML service indicators
- Five operational runbooks under `docs/runbooks/`
- OpenAPI docs for backend and ML service
- CI scaffolding for backend tests, frontend build, and Python tests

## Start
Requires Docker Desktop with Docker Compose.

```bash
cp .env.example .env
docker compose up --build
```

- Analyst UI: http://localhost:5173
- Java API: http://localhost:8080
- Java health: http://localhost:8080/actuator/health
- Java Swagger: http://localhost:8080/swagger-ui/index.html
- ML API docs: http://localhost:8001/docs

## Demo flow
1. Open the analyst UI.
2. Click **Generate demo alert** to create a transaction and run it through rule and ML scoring.
3. Open the alert and move it to `INVESTIGATING`.
4. Resolve it as confirmed fraud or false positive; the transition is written to the audit table.
5. Inspect the alert's history and masked card reference.

## Local development
Backend:
```bash
cd backend
mvn spring-boot:run
```
ML service:
```bash
cd ml-service
python -m venv .venv
# activate the venv, then:
pip install -r requirements.txt
uvicorn app:app --reload --port 8001
```
Frontend:
```bash
cd frontend
npm install
npm run dev
```

## Security and scope
This is a demo, not a production fraud platform. It does not connect to AWS SQS or CloudWatch; the local event endpoint and health checks simulate the event-ingestion flow. The included model is trained on synthetic demo data and must not be used for real fraud decisions. PANs are not stored; use synthetic test values only. Authentication/authorization, secrets management, evidence file storage, production monitoring, and PCI DSS review are not implemented. Latency and coverage targets are not claimed as measured results.
# FraudAlert-Real-Time-Fraud-Alert-Case-Management-System
