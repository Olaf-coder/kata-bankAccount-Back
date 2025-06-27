package com.kata.bankaccountback.domain.mapper;

import com.kata.bankaccountback.domain.model.dto.BalanceDto;
import com.kata.bankaccountback.domain.model.entity.BalanceEntity;
import org.springframework.stereotype.Component;

@Component
public class BalanceMapper {
    public BalanceDto toDto(BalanceEntity entity) {
        return new BalanceDto(
                entity.getId(),
                entity.getDate(),
                entity.getBalance()
        );
    }

    public BalanceEntity toEntity(BalanceDto dto) {
        BalanceEntity entity = new BalanceEntity();
        entity.setId(dto.id());
        entity.setDate(dto.date());
        entity.setBalance(dto.balance());
        return entity;
    }
}
