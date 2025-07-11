package com.kata.bankaccountback.service;

import com.kata.bankaccountback.domain.mapper.BalanceMapper;
import com.kata.bankaccountback.domain.model.dto.BalanceDto;
import com.kata.bankaccountback.domain.model.entity.BalanceEntity;
import com.kata.bankaccountback.domain.repository.BalanceRepository;
import com.kata.bankaccountback.exceptions.RessourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {

    private static final Logger log = LoggerFactory.getLogger(BalanceServiceTest.class);
    private BalanceServiceImpl balanceService;

    @Mock
    private BalanceMapper balanceMapper;

    @Mock
    private BalanceRepository balanceRepository;


    @BeforeEach
    void setUp() {
        balanceService = Mockito.spy(new BalanceServiceImpl(balanceMapper, balanceRepository));
    }

    @Test
    void SHOULD_call_findById_once_WHEN_getBalanceById_is_called_and_balance_is_found() {
        //GIVEN
        BalanceEntity foundEntity = createBalanceEntity(1L, LocalDate.now(), BigDecimal.TEN);
        BalanceDto expectedDto = new BalanceDto(1L, LocalDate.now(), BigDecimal.TEN);

        when(balanceMapper.toDto(foundEntity)).thenReturn(expectedDto);
        when(balanceRepository.findById(1L)).thenReturn(Optional.of(foundEntity));

        //WHEN
        BalanceDto actualDto = balanceService.getBalanceById(1L);

        //THEN
        assertThat(actualDto).isEqualTo(expectedDto);
        verify(balanceRepository, times(1)).findById(1L);
    }

    @Test
    void SHOULD_call_findById_once_and_throw_RessourceNotFoundException_WHEN_getBalanceById_is_called_and_balance_is_not_found() {
        //GIVEN

        when(balanceRepository.findById(1L)).thenReturn(Optional.empty());

        //WHEN //THEN
        assertThatExceptionOfType(RessourceNotFoundException.class).isThrownBy(() -> balanceService.getBalanceById(1L));
        verify(balanceRepository, times(1)).findById(1L);
    }

    @Test
    void SHOULD_call_getBalanceById_once_WHEN_getFirstBalance_is_called() {
        //GIVEN
        BalanceEntity foundEntity = createBalanceEntity(1L, LocalDate.now(), BigDecimal.TEN);
        BalanceDto expectedDto = new BalanceDto(1L, LocalDate.now(), BigDecimal.TEN);

        when(balanceMapper.toDto(foundEntity)).thenReturn(expectedDto);
        when(balanceRepository.findById(1L)).thenReturn(Optional.of(foundEntity));
        //WHEN
        BalanceDto actualDto = balanceService.getFirstBalance();

        //THEN
        verify(balanceRepository, times(1)).findById(1L);
        verify(balanceService, times(1)).getBalanceById(1L);
        assertThat(actualDto).isEqualTo(expectedDto);
    }

    @Test
    void SHOULD_call_findById_and_balanceRepositorySave_WHEN_updateBalance_is_called_with_correctValue() {
        //GIVEN
        BalanceEntity inputEntity = createBalanceEntity(1L, LocalDate.now().minusDays(1), BigDecimal.TEN);
        BigDecimal newAmount = BigDecimal.ZERO;
        LocalDate newDate = LocalDate.now();
        BalanceEntity savedEntity = createBalanceEntity(1L, newDate, newAmount);
        BalanceDto expectedUpdatedDto = new BalanceDto(1L, newDate, newAmount);

        ArgumentCaptor<BalanceEntity> entityCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        when(balanceRepository.findById(1L)).thenReturn(Optional.of(inputEntity));
        when(balanceRepository.save(entityCaptor.capture())).thenReturn(savedEntity);
        when(balanceMapper.toDto(savedEntity)).thenReturn(expectedUpdatedDto);

        //WHEN
        BalanceDto actualUpdatedDto = balanceService.updateBalance(1L, BigDecimal.ZERO, LocalDate.now());

        //THEN
        verify(balanceRepository, times(1)).findById(1L);
        verify(balanceRepository, times(1)).save(entityCaptor.capture());
        verify(balanceMapper, times(1)).toDto(savedEntity);
        //il faut faire un times sur balanceentity.setBalance(newAmount) et balanceentity.setDate(newDate); durant le update
        assertThat(entityCaptor.getValue()).isEqualTo(savedEntity);
        assertThat(actualUpdatedDto).isEqualTo(expectedUpdatedDto);

    }


//    @Test
//    public void SHOULD_call_findById_and_balanceRepositorySave_WHEN_updateBalance_is_called_with_correctValue_withSpy() {
//        //GIVEN
//        BalanceEntity inputEntity = createBalanceEntity(1L, LocalDate.now().minus(1, ChronoUnit.DAYS), BigDecimal.TEN);
//        BalanceEntity spyEntity = Mockito.spy(inputEntity);
//        BigDecimal newAmount = BigDecimal.ZERO;
//        LocalDate newDate = LocalDate.now();
//        BalanceEntity savedEntity = createBalanceEntity(1L, newDate, newAmount);
//        BalanceDto expectedUpdatedDto = new BalanceDto(1L, newDate, newAmount);
//
//        when(balanceRepository.findById(1L)).thenReturn(Optional.of(spyEntity));
//        when(balanceRepository.save(spyEntity)).thenReturn(spyEntity);
//        when(balanceMapper.toDto(savedEntity)).thenReturn(expectedUpdatedDto);
//
//        //WHEN
//        BalanceDto actualUpdatedDto = balanceService.updateBalance(1L, BigDecimal.ZERO, LocalDate.now());
//
//        //THEN
//        verify(balanceRepository, times(1)).findById(1L);
//        verify(balanceRepository, times(1)).save(spyEntity);
//        verify(balanceMapper, times(1)).toDto(savedEntity);
//        //il faut faire un times sur balanceentity.setBalance(newAmount) et balanceentity.setDate(newDate); durant le update
//        verify(spyEntity, times(1)).setBalance(newAmount);
//        verify(spyEntity, times(1)).setDate(newDate);
//        assertThat(actualUpdatedDto).isEqualTo(expectedUpdatedDto);
//
//    }


    @Test
    void SHOULD_call_findById_once_and_throw_RessourceNotFoundException_WHEN_updateBalance_is_called_and_balance_is_not_found() {
        //GIVEN

        when(balanceRepository.findById(1L)).thenReturn(Optional.empty());

        //WHEN //THEN
        assertThatExceptionOfType(RessourceNotFoundException.class).isThrownBy(() -> balanceService.updateBalance(1L, BigDecimal.TEN, LocalDate.now()));
        verify(balanceRepository, times(1)).findById(1L);
    }

    private BalanceEntity createBalanceEntity(Long id, LocalDate date, BigDecimal amount) {
        BalanceEntity entity = new BalanceEntity();
        entity.setId(id);
        entity.setDate(date);
        entity.setBalance(amount);
        return entity;
    }
}
