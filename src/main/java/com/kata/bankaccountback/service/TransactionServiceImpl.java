package com.kata.bankaccountback.service;

import com.kata.bankaccountback.domain.mapper.TransactionMapper;
import com.kata.bankaccountback.domain.model.dto.TransactionDto;
import com.kata.bankaccountback.domain.model.entity.TransactionEntity;
import com.kata.bankaccountback.domain.repository.TransactionRepository;
import com.kata.bankaccountback.exceptions.InvalidDataException;
import com.kata.bankaccountback.exceptions.RessourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    public static final String TRANSACTION_AMOUNTS_ARE_NOT_CORRECT = "Transaction amounts are not correct";
    public static final String NO_TRANSACTIONS_FOUND = "No transactions found";
    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionMapper transactionMapper, TransactionRepository transactionRepository) {
        this.transactionMapper = transactionMapper;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    @Override
    public TransactionDto addTransaction(TransactionDto transaction) throws InvalidDataException {
        if (!isTransactionAmountsCorrect(transaction)) {
            throw new InvalidDataException(TRANSACTION_AMOUNTS_ARE_NOT_CORRECT);
        }

        List<TransactionEntity> transactions = transactionRepository.findAll();
        BigDecimal newBalance = BigDecimal.ZERO;
        if (!transactions.isEmpty()) {
            BigDecimal sumWithdrawal = transactions.stream().map(TransactionEntity::getWithdrawAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal sumDeposit = transactions.stream().map(TransactionEntity::getDepositAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            newBalance = sumDeposit.subtract(sumWithdrawal);
        }
        newBalance = newBalance.add(transaction.depositAmount()).subtract(transaction.withdrawAmount());

        TransactionDto result = new TransactionDto(null, null, transaction.depositAmount(), transaction.withdrawAmount(), newBalance);
        return transactionMapper.toDto(transactionRepository.save(transactionMapper.toEntity(result)));
    }

    @Override
    public List<TransactionDto> getAllTransactions() throws RessourceNotFoundException {
        List<TransactionEntity> transactions = transactionRepository.findAll();
        if (transactions.isEmpty()) {
            throw new RessourceNotFoundException(NO_TRANSACTIONS_FOUND);
        }
        return transactions.stream().map(transactionMapper::toDto).toList();
    }

    private boolean isTransactionAmountsCorrect(TransactionDto trans) {
        return ((isPositive(trans.withdrawAmount()) && isZero(trans.depositAmount()))
                || (isPositive(trans.depositAmount()) && isZero(trans.withdrawAmount())));
    }

    private boolean isPositive(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    private boolean isZero(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }

}
