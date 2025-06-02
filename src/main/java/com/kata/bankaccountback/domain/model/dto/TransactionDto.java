package com.kata.bankaccountback.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionDto(
        @Schema(description="Unique and auto-generated identifier of the transaction", accessMode = Schema.AccessMode.READ_ONLY)
        @Nullable
        Long id,

        @Schema(description = "Transaction date", example = "2025-12-31", format = "date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        @Nullable
        LocalDate date,

        @Schema(description="Deposit amount")
        @Nullable
        BigDecimal depositAmount,

        @Schema(description="Withdrawal amount")
        @Nullable
        BigDecimal withdrawAmount
)
{}
