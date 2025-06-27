package com.kata.bankaccountback.service;

import com.kata.bankaccountback.domain.model.dto.BalanceDto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface BalanceService {
    BalanceDto getBalanceById(Long balanceId);

    BalanceDto getFirstBalance();

    BalanceDto updateBalance(Long balanceId, BigDecimal newAmount, LocalDate newDate);
}
