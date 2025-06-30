package com.kata.bankaccountback.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kata.bankaccountback.domain.model.dto.BalanceDto;
import com.kata.bankaccountback.domain.model.dto.TransactionDto;
import com.kata.bankaccountback.exceptions.RessourceNotFoundException;
import com.kata.bankaccountback.service.BalanceService;
import com.kata.bankaccountback.service.BalanceServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = BalanceController.class)
public class BalanceControllerTest {

    private static final String ENDPOINT = "/v1.0/balances";

    @MockitoBean
    private final BalanceService balanceService = Mockito.mock(BalanceServiceImpl.class);

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void tearDown() {
        Mockito.reset(balanceService);
    }

    //READ
    //SUCCESS
    @Test
    void should_call_balanceService_and_return_200_and_Balance_when_GET_balances_is_called() throws Exception{
        //GIVEN
        BalanceDto balanceDto = new BalanceDto(1L, LocalDate.now(), BigDecimal.TEN);
        Mockito.when(balanceService.getFirstBalance()).thenReturn(balanceDto);

        //WHEN THEN
        mockMvc.perform(get(ENDPOINT + "/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(balanceDto.id().intValue())))
                .andExpect(jsonPath("$.date", is(balanceDto.date().toString())))
                .andExpect(jsonPath("$.balance", is(balanceDto.balance().intValue())))
                .andReturn();
    }

    //FAILED
    @Test
    void should_call_getAllTransactions_and_return_404_when_GET_contacts_called() throws Exception {
        //GIVEN
        String message = "No balance Found";
        Mockito.when(balanceService.getFirstBalance()).thenThrow(new RessourceNotFoundException(message));

        //WHEN THEN
        mockMvc.perform(get(ENDPOINT +"/"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.code", is(HttpStatus.NOT_FOUND.value())));
    }

}
