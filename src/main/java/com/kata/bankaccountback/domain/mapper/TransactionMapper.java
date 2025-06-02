package com.kata.bankaccountback.domain.mapper;

import com.kata.bankaccountback.domain.model.dto.TransactionDto;
import com.kata.bankaccountback.domain.model.entity.TransactionEntity;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public TransactionDto toDto(TransactionEntity entity) {
        return new TransactionDto(
                entity.getId(),
                entity.getDate(),
                entity.getDepositAmount(),
                entity.getWithdrawAmount(),
                entity.getBalance()
        );
    }

    public TransactionEntity toEntity(TransactionDto dto) {
        TransactionEntity entity = new TransactionEntity();
        entity.setId(dto.id());
        entity.setDate(dto.date());
        entity.setDepositAmount(dto.depositAmount());
        entity.setWithdrawAmount(dto.withdrawAmount());
        entity.setBalance(dto.balance());
        return entity;
    }
}
