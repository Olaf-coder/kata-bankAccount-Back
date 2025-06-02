package com.kata.bankaccountback.domain.mapper;

import com.kata.bankaccountback.domain.model.dto.TransactionDto;
import com.kata.bankaccountback.domain.model.entity.TransactionEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;


public class TransactionMapperTest {

    private TransactionMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new TransactionMapper();
    }

    @Test
    void SHOULD_return_complete_transactionDto_WHEN_toDto_is_called_with_complete_transactionEntity() {
        TransactionDto dtoActual = mapper.toDto(generateExpectedCompleteEntity());
        Assertions.assertEquals(generateExpectedCompleteDto(), dtoActual);
    }

    @Test
    void SHOULD_return_complete_transactionEntity_WHEN_toEntity_is_called_with_complete_transactionDto() {
        TransactionEntity entityActual = mapper.toEntity(generateExpectedCompleteDto());
        Assertions.assertEquals(generateExpectedCompleteEntity(), entityActual);
    }

    private TransactionDto generateExpectedCompleteDto() {
        return new TransactionDto(1L, LocalDate.now(), BigDecimal.valueOf(10), BigDecimal.valueOf(0));
    }



    private TransactionEntity generateExpectedCompleteEntity() {
        TransactionEntity entity = new TransactionEntity();

        entity.setId(1L);
        entity.setDate(LocalDate.now());
        entity.setDepositAmount(BigDecimal.valueOf(10));
        entity.setWithdrawAmount(BigDecimal.valueOf(0));
        return entity;
    }

}
