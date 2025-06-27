package com.kata.bankaccountback.service;

import com.kata.bankaccountback.domain.model.dto.TransactionDto;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    TransactionDto addTransaction(TransactionDto transaction);
    TransactionDto addWithdraw(BigDecimal amountWithdrawal);
    TransactionDto addDeposit(BigDecimal amountDeposit);
    List<TransactionDto> getAllTransactions();

}
