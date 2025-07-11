package com.kata.bankaccountback.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kata.bankaccountback.advice.GlobalControllerAdvice;
import com.kata.bankaccountback.domain.model.dto.TransactionDto;
import com.kata.bankaccountback.exceptions.InvalidDataException;
import com.kata.bankaccountback.exceptions.RessourceNotFoundException;
import com.kata.bankaccountback.service.TransactionService;
import com.kata.bankaccountback.service.TransactionServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = TransactionController.class)
@Import(GlobalControllerAdvice.class)
class TransactionControllerTest {


    private static final String ENDPOINT = "/v1.0/transactions";

    @MockitoBean
    private final TransactionService transactionService = Mockito.mock(TransactionServiceImpl.class);

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void tearDown() {
        Mockito.reset(transactionService);
    }

//CREATE
    //SUCCESS
@Test
void should_call_addTransaction_and_return_201_and_Transaction_when_POST_transaction_with_transaction_requestbody_is_called() throws Exception {
    //GIVEN
    TransactionDto transactioninput = new TransactionDto(null, null, BigDecimal.TEN, BigDecimal.ZERO, null);
    TransactionDto transactionsaved = new TransactionDto(1L, LocalDate.now(), BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
    Mockito.when(transactionService.addTransaction(transactioninput)).thenReturn(transactionsaved);

    //WHEN THEN
    mockMvc.perform(post(ENDPOINT + "/").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(transactioninput)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(transactionsaved.id().intValue())))
            .andExpect(jsonPath("$.date", is(transactionsaved.date().toString())))
            .andExpect(jsonPath("$.depositAmount", is(transactionsaved.depositAmount().intValue())))
            .andExpect(jsonPath("$.withdrawAmount", is(transactionsaved.withdrawAmount().intValue())))
            .andExpect(jsonPath("$.balance", is(transactionsaved.balance().intValue())))
            .andReturn();
}

    //FAILED
    @Test
    void should_call_addTransaction_and_return_400_when_POST_transactions_with_bad_Transaction_requestbody_is_called() throws Exception {
        //GIVEN
        String message = "Bad Datas";
        TransactionDto transactionBad = new TransactionDto(null, null, null, null, null);
        Mockito.when(transactionService.addTransaction(transactionBad)).thenThrow(new InvalidDataException(message));

        //WHEN THEN
        mockMvc.perform(post(ENDPOINT + "/").content(new ObjectMapper().writeValueAsString(transactionBad)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value())))
                .andReturn();
    }

//READ
    //SUCCESS
    @Test
    void should_call_transactionService_and_return_200_and_Transactions_when_GET_transactionss_is_called() throws Exception{
        //GIVEN
        List<TransactionDto> transactions = List.of(
                new TransactionDto(1L, LocalDate.now(), BigDecimal.TEN, BigDecimal.ZERO , BigDecimal.TEN),
                new TransactionDto(2L, LocalDate.now(), BigDecimal.ZERO, BigDecimal.ONE , BigDecimal.valueOf(9)),
                new TransactionDto(3L, LocalDate.now(), BigDecimal.TWO, BigDecimal.ZERO , BigDecimal.valueOf(11))
        );
        Mockito.when(transactionService.getAllTransactions()).thenReturn(transactions);

        //WHEN THEN
        mockMvc.perform(get(ENDPOINT + "/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].date", is(LocalDate.now().toString())))
                .andExpect(jsonPath("$.[0].depositAmount", is(10)))
                .andExpect(jsonPath("$.[0].withdrawAmount", is(0)))
                .andExpect(jsonPath("$.[0].balance", is(10)))
                .andReturn();
    }

    //FAILED
    @Test
    void should_call_getAllTransactions_and_return_404_when_GET_contacts_called() throws Exception {
        //GIVEN
        String message = "Transactions not found";
        Mockito.when(transactionService.getAllTransactions()).thenThrow(new RessourceNotFoundException(message));

        //WHEN THEN
        mockMvc.perform(get(ENDPOINT +"/"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.code", is(HttpStatus.NOT_FOUND.value())));
    }

//DEPOSITS
    //SUCCESS
    @Test
    void SHOULD_call_addDeposit_and_return_201_WHEN_POST_deposit_with_BigDecimal_RequestBody_is_called() throws Exception {
        //GIVEN
        BigDecimal newDeposit = BigDecimal.TEN;
        TransactionDto transactionsaved = new TransactionDto(1L, LocalDate.now(), BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
        Mockito.when(transactionService.addDeposit(newDeposit)).thenReturn(transactionsaved);

        //WHEN THEN
        mockMvc.perform(post(ENDPOINT + "/deposits/").contentType(MediaType.APPLICATION_JSON).content(newDeposit.toString()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(transactionsaved.id().intValue())))
                .andExpect(jsonPath("$.date", is(transactionsaved.date().toString())))
                .andExpect(jsonPath("$.depositAmount", is(transactionsaved.depositAmount().intValue())))
                .andExpect(jsonPath("$.withdrawAmount", is(transactionsaved.withdrawAmount().intValue())))
                .andExpect(jsonPath("$.balance", is(transactionsaved.balance().intValue())))
                .andReturn();
    }

    //FAILED
    @Test
    void SHOULD_call_addDeposit_and_return_400_WHEN_POST_deposit_with_wrong_BigDecimal_RequestBody_is_called() throws Exception {
        //GIVEN
        String message = "Bad Datas";
        BigDecimal badDeposit = BigDecimal.ZERO;
        Mockito.when(transactionService.addDeposit(badDeposit)).thenThrow(new InvalidDataException(message));

        //WHEN THEN
        mockMvc.perform(post(ENDPOINT + "/deposits/").content(badDeposit.toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value())))
                .andReturn();
    }

    //WITHDRAW
    //SUCCESS
    @Test
    void SHOULD_call_addWithdraw_and_return_201_WHEN_POST_withdraw_with_BigDecimal_RequestBody_is_called() throws Exception {
        //GIVEN
        BigDecimal newWithdraw = BigDecimal.TEN;
        TransactionDto transactionsaved = new TransactionDto(1L, LocalDate.now(), BigDecimal.ZERO, BigDecimal.TEN, BigDecimal.ZERO);
        Mockito.when(transactionService.addWithdraw(newWithdraw)).thenReturn(transactionsaved);

        //WHEN THEN
        mockMvc.perform(post(ENDPOINT + "/withdrawals/").contentType(MediaType.APPLICATION_JSON).content(newWithdraw.toString()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(transactionsaved.id().intValue())))
                .andExpect(jsonPath("$.date", is(transactionsaved.date().toString())))
                .andExpect(jsonPath("$.depositAmount", is(transactionsaved.depositAmount().intValue())))
                .andExpect(jsonPath("$.withdrawAmount", is(transactionsaved.withdrawAmount().intValue())))
                .andExpect(jsonPath("$.balance", is(transactionsaved.balance().intValue())))
                .andReturn();
    }

    //FAILED
    @Test
    void SHOULD_call_addWithdraw_and_return_400_WHEN_POST_withdrawals_with_wrong_BigDecimal_RequestBody_is_called() throws Exception {
        //GIVEN
        String message = "Bad Datas";
        BigDecimal badWithdraw = BigDecimal.ZERO;
        Mockito.when(transactionService.addWithdraw(badWithdraw)).thenThrow(new InvalidDataException(message));

        //WHEN THEN
        mockMvc.perform(post(ENDPOINT + "/withdrawals/").content(badWithdraw.toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value())))
                .andReturn();
    }
}
