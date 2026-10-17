package com.example.fraudalert.alert;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AlertRepository extends JpaRepository<FraudAlert, Long> {
    Optional<FraudAlert> findByTransactionId(String transactionId);
    List<FraudAlert> findAllByOrderByRiskScoreDescCreatedAtDesc();
}
