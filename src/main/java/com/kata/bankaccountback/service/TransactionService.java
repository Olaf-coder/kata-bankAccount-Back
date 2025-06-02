package com.kata.bankaccountback.service;

import com.kata.bankaccountback.domain.model.dto.TransactionDto;

import java.util.List;

public interface TransactionService {
    TransactionDto addTransaction(TransactionDto transaction);
    List<TransactionDto> getAllTransactions();

}
