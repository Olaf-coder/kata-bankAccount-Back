package com.kata.bankaccountback.service;

import com.kata.bankaccountback.domain.mapper.TransactionMapper;
import com.kata.bankaccountback.domain.model.dto.BalanceDto;
import com.kata.bankaccountback.domain.model.dto.TransactionDto;
import com.kata.bankaccountback.domain.model.entity.TransactionEntity;
import com.kata.bankaccountback.domain.repository.TransactionRepository;
import com.kata.bankaccountback.exceptions.InvalidDataException;
import com.kata.bankaccountback.exceptions.RessourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final String TRANSACTION_AMOUNTS_ARE_NOT_CORRECT = "Transaction amounts are not correct";

    private static final String DEPOSIT_AMOUNT_IS_NOT_CORRECT = "Deposit amount is not correct";
    private static final String WITHDRAW_AMOUNT_IS_NOT_CORRECT = "Withdraw amount is not correct";
    private static final String NO_TRANSACTIONS_FOUND = "No transactions found";
    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;
    private final BalanceService balanceService;

    public TransactionServiceImpl(TransactionMapper transactionMapper, TransactionRepository transactionRepository, BalanceService balanceService) {
        this.transactionMapper = transactionMapper;
        this.transactionRepository = transactionRepository;
        this.balanceService = balanceService;
    }

    @Transactional
    @Override
    public TransactionDto addTransaction(TransactionDto transaction) throws InvalidDataException {
        if (!isTransactionAmountsCorrect(transaction)) {
            throw new InvalidDataException(TRANSACTION_AMOUNTS_ARE_NOT_CORRECT);
        }

        List<TransactionEntity> transactions = transactionRepository.findAll();
        BigDecimal newBalance = BigDecimal.ZERO;
        //TODO: utiliser BalanceService a la place.
        if (!transactions.isEmpty()) {
            BigDecimal sumWithdrawal = transactions.stream().map(TransactionEntity::getWithdrawAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal sumDeposit = transactions.stream().map(TransactionEntity::getDepositAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            newBalance = sumDeposit.subtract(sumWithdrawal);
        }

        BigDecimal realDepositAmount = getRealDepositAmount(transaction.depositAmount());
        BigDecimal realWithdrawAmount = getRealDepositAmount(transaction.withdrawAmount());

        newBalance = newBalance.add(realDepositAmount).subtract(realWithdrawAmount);

        TransactionDto result = new TransactionDto(null, null, transaction.depositAmount(), transaction.withdrawAmount(), newBalance);
        return transactionMapper.toDto(transactionRepository.save(transactionMapper.toEntity(result)));
    }

    @Transactional
    @Override
    public TransactionDto addWithdraw(BigDecimal amountWithdrawal) {
        if (!isPositive(amountWithdrawal))
            throw new InvalidDataException(WITHDRAW_AMOUNT_IS_NOT_CORRECT);
        BalanceDto balance = balanceService.getFirstBalance();
        BigDecimal newBalance = balance.balance().subtract(amountWithdrawal);
        balanceService.updateBalance(balance.id(), newBalance, LocalDate.now());

        TransactionDto result = new TransactionDto(null, null, BigDecimal.ZERO, amountWithdrawal, newBalance);
        return transactionMapper.toDto(transactionRepository.save(transactionMapper.toEntity(result)));
    }

    @Override
    public TransactionDto addDeposit(BigDecimal amountDeposit) {
        if (!isPositive(amountDeposit))
            throw new InvalidDataException(DEPOSIT_AMOUNT_IS_NOT_CORRECT);
        BalanceDto balance = balanceService.getFirstBalance();
        BigDecimal newBalance = balance.balance().add(amountDeposit);
        balanceService.updateBalance(balance.id(), newBalance, LocalDate.now());

        TransactionDto result = new TransactionDto(null, null, amountDeposit, BigDecimal.ZERO, newBalance);
        return transactionMapper.toDto(transactionRepository.save(transactionMapper.toEntity(result)));
    }

    //TODO Retourner au pire une liste vide, pas un exception.

    @Override
    public List<TransactionDto> getAllTransactions() throws RessourceNotFoundException {
        List<TransactionEntity> transactions = transactionRepository.findAll();
        if (transactions.isEmpty()) {
            throw new RessourceNotFoundException(NO_TRANSACTIONS_FOUND);
        }
        return transactions.stream().map(transactionMapper::toDto).toList();
    }

    private boolean isTransactionAmountsCorrect(TransactionDto trans) {
        return ((isPositive(trans.withdrawAmount()) && isZeroOrNull(trans.depositAmount()))
                || (isPositive(trans.depositAmount()) && isZeroOrNull(trans.withdrawAmount())));
    }


    private boolean isPositive(BigDecimal amount) {
        //NON NULL et positif
        return (amount != null) && (amount.compareTo(BigDecimal.ZERO) > 0);
    }

    private boolean isZeroOrNull(BigDecimal amount) {
        return (amount == null) || (amount.compareTo(BigDecimal.ZERO) == 0);

    }

    private BigDecimal getRealDepositAmount(BigDecimal transaction) {
        return transaction != null ? transaction : BigDecimal.ZERO;
    }

}
