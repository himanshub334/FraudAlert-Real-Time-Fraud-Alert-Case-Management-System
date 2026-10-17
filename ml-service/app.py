from fastapi import FastAPI
from pydantic import BaseModel, Field
from sklearn.ensemble import RandomForestClassifier
import numpy as np

app = FastAPI(title="FraudAlert ML Scoring API", version="1.0.0")

# Synthetic examples only. Not suitable for real fraud decisions.
_rng = np.random.default_rng(42)
X = np.column_stack([
    _rng.lognormal(4.0, 1.2, 1500),
    _rng.integers(0, 10, 1500),
    _rng.integers(0, 2, 1500),
    _rng.integers(0, 2, 1500),
])
risk = (X[:, 0] > 1800) | (X[:, 1] >= 5) | (X[:, 2] == 1) | (X[:, 3] == 1)
y = (risk & (_rng.random(1500) > 0.18)).astype(int)
_model = RandomForestClassifier(n_estimators=60, max_depth=7, random_state=42, n_jobs=1)
_model.fit(X, y)

class ScoreRequest(BaseModel):
    amount: float = Field(ge=0)
    prior_transactions: int = Field(ge=0)
    high_risk_category: bool = False
    unusual_country: bool = False

@app.get("/health")
def health():
    return {"status": "UP", "model": "synthetic-demo-random-forest"}

@app.post("/score")
def score(request: ScoreRequest):
    features = np.array([[
        request.amount, request.prior_transactions,
        int(request.high_risk_category), int(request.unusual_country)
    ]])
    probability = float(_model.predict_proba(features)[0][1])
    return {"fraud_probability": round(probability, 6), "model": "synthetic-demo-random-forest"}
