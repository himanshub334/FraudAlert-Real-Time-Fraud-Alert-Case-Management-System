package com.example.fraudalert.alert;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record TransactionRequest(
    @NotBlank String transactionId,
    @NotBlank String merchantId,
    @NotNull @DecimalMin("0.01") BigDecimal amount,
    @NotBlank String currency,
    @NotBlank String category,
    @NotBlank String country,
    @Min(0) int priorTransactionsInWindow,
    @NotBlank String cardReference
) {}
