package com.kata.bankaccountback.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BalanceDto(
        @Schema(description="Unique and auto-generated identifier of the balance", accessMode = Schema.AccessMode.READ_ONLY)
        @Nullable
        Long id,

        @Schema(description = "Transaction date", example = "2025-12-31", format = "date", accessMode = Schema.AccessMode.READ_ONLY)
        @JsonFormat(pattern = "yyyy-MM-dd")
        @Nullable
        LocalDate date,

        @Schema(description="Balance", accessMode = Schema.AccessMode.READ_ONLY)
        BigDecimal balance
) {}
