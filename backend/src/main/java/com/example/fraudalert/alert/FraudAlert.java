package com.example.fraudalert.alert;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "fraud_alerts", uniqueConstraints = @UniqueConstraint(columnNames = "transactionId"))
public class FraudAlert {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true) private String transactionId;
    @Column(nullable = false) private String merchantId;
    @Column(nullable = false, precision = 14, scale = 2) private BigDecimal amount;
    @Column(nullable = false) private String currency;
    @Column(nullable = false) private String category;
    @Column(nullable = false) private String country;
    @Column(nullable = false) private String maskedCardReference;
    @Column(nullable = false) private double riskScore;
    @Column(nullable = false) private String riskLevel;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private AlertStatus status = AlertStatus.OPEN;
    @Column(nullable = false) private String reason;
    @Column(nullable = false) private Instant createdAt = Instant.now();
    protected FraudAlert() {}
    public FraudAlert(TransactionRequest r, double score, String level, String reason) {
        transactionId = r.transactionId(); merchantId = r.merchantId(); amount = r.amount();
        currency = r.currency(); category = r.category(); country = r.country();
        maskedCardReference = mask(r.cardReference()); riskScore = score; riskLevel = level; this.reason = reason;
    }
    private static String mask(String value) {
        String digits = value == null ? "" : value.replaceAll("\\s", "");
        if (digits.length() <= 4) return "****";
        return "**** **** **** " + digits.substring(digits.length() - 4);
    }
    public Long getId(){return id;} public String getTransactionId(){return transactionId;}
    public String getMerchantId(){return merchantId;} public BigDecimal getAmount(){return amount;}
    public String getCurrency(){return currency;} public String getCategory(){return category;}
    public String getCountry(){return country;} public String getMaskedCardReference(){return maskedCardReference;}
    public double getRiskScore(){return riskScore;} public String getRiskLevel(){return riskLevel;}
    public AlertStatus getStatus(){return status;} public String getReason(){return reason;}
    public Instant getCreatedAt(){return createdAt;}
    public void setStatus(AlertStatus status){this.status=status;}
}
