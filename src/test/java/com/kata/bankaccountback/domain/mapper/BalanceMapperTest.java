package com.kata.bankaccountback.domain.mapper;


import com.kata.bankaccountback.domain.model.dto.BalanceDto;
import com.kata.bankaccountback.domain.model.entity.BalanceEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

class BalanceMapperTest {

    private BalanceMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new BalanceMapper();
    }

    @Test
    void SHOULD_return_complete_balanceEntity_WHEN_toEntity_is_called_with_complete_balanceDto() {
        BalanceDto dto = generateCompleteBalanceDto();
        Assertions.assertEquals(generateCompleteBalanceEntity(), mapper.toEntity(dto));
    }

    @Test
    void SHOULD_return_complete_balanceCto__WHEN_toDto_is_called_with_complete_balanceEntity() {
        BalanceEntity entity = generateCompleteBalanceEntity();
        Assertions.assertEquals(generateCompleteBalanceDto(), mapper.toDto(entity));
    }


    private BalanceDto generateCompleteBalanceDto() {
        return new BalanceDto(1L, LocalDate.now(), BigDecimal.TEN);

    }

    private BalanceEntity generateCompleteBalanceEntity() {
        BalanceEntity entity = new BalanceEntity();
        entity.setId(1L);
        entity.setDate(LocalDate.now());
        entity.setBalance(BigDecimal.TEN);
        return entity;
    }
}
