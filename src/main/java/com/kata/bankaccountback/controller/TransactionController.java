package com.kata.bankaccountback.controller;

import com.kata.bankaccountback.domain.model.dto.TransactionDto;
import com.kata.bankaccountback.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("v1.0/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {this.transactionService = transactionService;}

    @GetMapping("/") @Operation(summary = "Get all transactions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions received",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TransactionDto.class)
                    )}),
            @ApiResponse(responseCode = "404", description = "Reception Failed",
                    content = @Content)
    })
    public ResponseEntity<List<TransactionDto>> getTransactions() {
        return new ResponseEntity<>(transactionService.getAllTransactions(), HttpStatus.OK);
    }

    @PostMapping("/")
    @Operation(summary = "Add new transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction Added",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TransactionDto.class)
                    )}),
            @ApiResponse(responseCode = "400", description = "Transaction failed", content = @Content )
    })
    public ResponseEntity<TransactionDto> createTransaction(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Transaction to add") @RequestBody TransactionDto transaction) {
        return new ResponseEntity<>(transactionService.addTransaction(transaction), HttpStatus.CREATED);
    }

    @PostMapping("/withdrawals/")
    @Operation(summary = "Add new withdrawal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction Added",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TransactionDto.class)
                    )}),
            @ApiResponse(responseCode = "400", description = "Transaction failed", content = @Content )
    })
    public ResponseEntity<TransactionDto> createWithdrawal(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Amount to withdraw") @RequestBody BigDecimal amount) {
        return new ResponseEntity<>(transactionService.addWithdraw(amount), HttpStatus.CREATED);
    }

    @PostMapping("/deposits/")
    @Operation(summary = "Add new deposit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction Added",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TransactionDto.class)
                    )}),
            @ApiResponse(responseCode = "400", description = "Transaction failed", content = @Content )
    })
    public ResponseEntity<TransactionDto> createDeposit(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Amount to depose") @RequestBody BigDecimal amount) {
        return new ResponseEntity<>(transactionService.addDeposit(amount), HttpStatus.CREATED);
    }
}
