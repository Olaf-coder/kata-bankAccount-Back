package com.kata.bankaccountback.service;

import com.kata.bankaccountback.domain.mapper.BalanceMapper;
import com.kata.bankaccountback.domain.model.dto.BalanceDto;
import com.kata.bankaccountback.domain.model.entity.BalanceEntity;
import com.kata.bankaccountback.domain.repository.BalanceRepository;
import com.kata.bankaccountback.exceptions.RessourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class BalanceServiceImpl implements BalanceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BalanceServiceImpl.class);

    private static final String NO_BALANCE_FOUND = "No balance Found";
    private final BalanceMapper balanceMapper;
    private final BalanceRepository balanceRepository;

    public BalanceServiceImpl(BalanceMapper balanceMapper, BalanceRepository balanceRepository) {
        this.balanceMapper = balanceMapper;
        this.balanceRepository = balanceRepository;
    }

    @Override
    public BalanceDto getBalanceById(Long balanceId) {
        LOGGER.info("getBalanceById called");
        BalanceEntity balanceentity = balanceRepository.findById(balanceId).orElseThrow(()-> new RessourceNotFoundException(NO_BALANCE_FOUND));
        return balanceMapper.toDto(balanceentity);
    }

    @Override
    public BalanceDto getFirstBalance() {
        LOGGER.info("Get first balance called");
        return getBalanceById(1L);
    }

    @Override
    public BalanceDto updateBalance(Long balanceId, BigDecimal newAmount, LocalDate newDate) {
        LOGGER.info("updateBalance is called");
        BalanceEntity balanceentity = balanceRepository.findById(balanceId).orElseThrow(()-> new RessourceNotFoundException(NO_BALANCE_FOUND));
        balanceentity.setBalance(newAmount);
        balanceentity.setDate(newDate);

        LOGGER.info("balance Updated");
        return balanceMapper.toDto(balanceRepository.save(balanceentity));
    }
}
