package com.kata.bankaccountback.service;

import com.kata.bankaccountback.domain.mapper.TransactionMapper;
import com.kata.bankaccountback.domain.model.dto.TransactionDto;
import com.kata.bankaccountback.domain.model.entity.TransactionEntity;
import com.kata.bankaccountback.domain.repository.TransactionRepository;
import com.kata.bankaccountback.exceptions.InvalidDataException;
import com.kata.bankaccountback.exceptions.RessourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    private TransactionServiceImpl transactionService;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionServiceImpl(transactionMapper, transactionRepository);
    }

    //CREATE
    @Test
    void SHOULD_call_save_once_with_correct_value_deposit_and_return_saved_transaction_WHEN_addTransaction_is_called_with_correct_transaction() {
        //GIVEN
        TransactionDto inputDto = new TransactionDto(null, null, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
        TransactionDto expectedDto = new TransactionDto(1L, LocalDate.now(), BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);

        TransactionEntity inputEntity = createTransactionEntity(null, null, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
        TransactionEntity savedEntity = createTransactionEntity(1L, LocalDate.now(), BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);

        when(transactionMapper.toEntity(inputDto)).thenReturn(inputEntity);
        when(transactionRepository.save(inputEntity)).thenReturn(savedEntity);
        when(transactionMapper.toDto(savedEntity)).thenReturn(expectedDto);

        //WHEN
        TransactionDto actualDto = transactionService.addTransaction(inputDto);

        //THEN
        verify(transactionMapper, times(1)).toEntity(inputDto);
        verify(transactionRepository, times(1)).save(inputEntity);
        verify(transactionMapper, times(1)).toDto(savedEntity);
        assertThat(actualDto).isEqualTo(expectedDto);
    }

    @Test
    void SHOULD_call_save_once_with_correct_value_withdraw_and_return_saved_transaction_WHEN_addTransaction_is_called_with_correct_transaction() {
        //GIVEN
        TransactionDto inputDto = new TransactionDto(null, null, BigDecimal.ZERO, BigDecimal.TEN, BigDecimal.valueOf(-10));
        TransactionDto expectedDto = new TransactionDto(1L, LocalDate.now(), BigDecimal.ZERO, BigDecimal.TEN, BigDecimal.valueOf(-10));

        TransactionEntity inputEntity = createTransactionEntity(null, null, BigDecimal.ZERO, BigDecimal.TEN, BigDecimal.valueOf(-10));
        TransactionEntity savedEntity = createTransactionEntity(1L, LocalDate.now(), BigDecimal.ZERO, BigDecimal.TEN, BigDecimal.valueOf(-10));

        when(transactionMapper.toEntity(inputDto)).thenReturn(inputEntity);
        when(transactionRepository.save(inputEntity)).thenReturn(savedEntity);
        when(transactionMapper.toDto(savedEntity)).thenReturn(expectedDto);

        //WHEN
        TransactionDto actualDto = transactionService.addTransaction(inputDto);

        //THEN
        verify(transactionMapper, times(1)).toEntity(inputDto);
        verify(transactionRepository, times(1)).save(inputEntity);
        verify(transactionMapper, times(1)).toDto(savedEntity);
        assertThat(actualDto).isEqualTo(expectedDto);
    }

    @Test
    void SHOULD_call_save_once_with_correct_value_and_return_saved_transaction_WHEN_addTransaction_is_called_with_correct_transaction_and_existing_Transaction_registered() {
        //GIVEN
        TransactionDto inputDto = new TransactionDto(null, null, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.valueOf(18));
        TransactionDto expectedDto = new TransactionDto(3L, LocalDate.now(), BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.valueOf(18));

        List<TransactionEntity> registeredEntities = List.of(
                createTransactionEntity(1L, LocalDate.now(), BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN),
                createTransactionEntity(2L, LocalDate.now(), BigDecimal.ZERO, BigDecimal.TWO, BigDecimal.valueOf(8))
        );

        TransactionEntity inputEntity = createTransactionEntity(null, null, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.valueOf(18));
        TransactionEntity savedEntity = createTransactionEntity(1L, LocalDate.now(), BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.valueOf(18));

        when(transactionRepository.findAll()).thenReturn(registeredEntities);
        when(transactionMapper.toEntity(inputDto)).thenReturn(inputEntity);
        when(transactionRepository.save(inputEntity)).thenReturn(savedEntity);
        when(transactionMapper.toDto(savedEntity)).thenReturn(expectedDto);

        //WHEN
        TransactionDto actualDto = transactionService.addTransaction(inputDto);

        //THEN
        verify(transactionMapper, times(1)).toEntity(inputDto);
        verify(transactionRepository, times(1)).save(inputEntity);
        verify(transactionMapper, times(1)).toDto(savedEntity);
        assertThat(actualDto).isEqualTo(expectedDto);


    }


    @ParameterizedTest
    @MethodSource("transactionProviderIncorrectForAddTransaction")
    void SHOULD_not_call_save_and_throw_InvalidDataException_WHEN_addTransaction_is_called_with_incorrect_transaction(TransactionDto inputWrongDto) {
        //GIVEN

        //WHEN THEN
        assertThatExceptionOfType(InvalidDataException.class).isThrownBy(() -> transactionService.addTransaction(inputWrongDto));
        verify(transactionRepository, never()).save(any(TransactionEntity.class));
    }

    //READ
    @Test
    void SHOULD_call_findAll_once_and_return_existing_transaction_WHEN_getAllTransactions_is_called_and_transactions_are_registered() {
        //GIVEN
        List<TransactionEntity> entities = createExpectedTransactionEntities();
        List<TransactionDto> expectedDtos = createExpectedTransactionDtos();

        when(transactionRepository.findAll()).thenReturn(entities);
        when(transactionMapper.toDto(any(TransactionEntity.class)))
                .thenReturn(expectedDtos.get(0), expectedDtos.get(1), expectedDtos.get(2));

        //WHEN
        List<TransactionDto> actualDtos = transactionService.getAllTransactions();

        //THEN
        verify(transactionRepository, times(1)).findAll();
        verify(transactionMapper, times(expectedDtos.size())).toDto(any(TransactionEntity.class));

        assertThat(actualDtos).isEqualTo(expectedDtos);
    }

    @Test
    void SHOULD_call_findAll_once_and_raise_NotFoundException_WHEN_getAllTransactions_is_called_and_no_transactions_registered() {
        //GIVEN
        List<TransactionEntity> existingEntities = new ArrayList<>();
        when(transactionRepository.findAll()).thenReturn(existingEntities);

        //WHEN THEN
        assertThatExceptionOfType(RessourceNotFoundException.class).isThrownBy(() -> transactionService.getAllTransactions());
        verify(transactionRepository, times(1)).findAll();
        verify(transactionMapper, never()).toDto(any(TransactionEntity.class));
    }


    private static Stream<Arguments> transactionProviderIncorrectForAddTransaction() {
        return Stream.of(
                Arguments.of(new TransactionDto(0L, null, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO)),
                Arguments.of(new TransactionDto(0L, null, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.ZERO)),
                Arguments.of(new TransactionDto(0L, null, BigDecimal.valueOf(-1), BigDecimal.ZERO, BigDecimal.ZERO)),
                Arguments.of(new TransactionDto(0L, null, BigDecimal.ZERO, BigDecimal.valueOf(-1), BigDecimal.ZERO))
        );
    }

    private List<TransactionDto> createExpectedTransactionDtos() {
        return List.of(
                new TransactionDto(1L, LocalDate.now(), BigDecimal.valueOf(10), BigDecimal.valueOf(0), BigDecimal.valueOf(10)),
                new TransactionDto(2L, LocalDate.now(), BigDecimal.valueOf(0), BigDecimal.valueOf(12), BigDecimal.valueOf(-2)),
                new TransactionDto(3L, LocalDate.now(), BigDecimal.valueOf(17), BigDecimal.valueOf(0), BigDecimal.valueOf(15))
        );
    }

    private List<TransactionEntity> createExpectedTransactionEntities() {

        return List.of(
                createTransactionEntity(1L, LocalDate.now(), BigDecimal.valueOf(10), BigDecimal.valueOf(0), BigDecimal.valueOf(10)),
                createTransactionEntity(2L, LocalDate.now(), BigDecimal.valueOf(0), BigDecimal.valueOf(12), BigDecimal.valueOf(-2)),
                createTransactionEntity(3L, LocalDate.now(), BigDecimal.valueOf(17), BigDecimal.valueOf(0), BigDecimal.valueOf(15))
        );
    }

    TransactionEntity createTransactionEntity(Long id, LocalDate date, BigDecimal deposit, BigDecimal withdrawal, BigDecimal balance) {
        TransactionEntity transactionEntity = new TransactionEntity();

        transactionEntity.setId(id);
        transactionEntity.setDate(date);
        transactionEntity.setWithdrawAmount(withdrawal);
        transactionEntity.setDepositAmount(deposit);
        transactionEntity.setBalance(balance);

        return transactionEntity;

    }

}
