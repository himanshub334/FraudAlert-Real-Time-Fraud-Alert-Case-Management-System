package com.example.fraudalert.alert;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CaseUpdateRequest(@NotNull AlertStatus status, @NotBlank String analystId, @Size(max=1000) String note) {}
