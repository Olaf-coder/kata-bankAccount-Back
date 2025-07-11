package com.kata.bankaccountback.controller;

import com.kata.bankaccountback.domain.model.dto.BalanceDto;
import com.kata.bankaccountback.service.BalanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1.0/balances")
public class BalanceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BalanceController.class);
    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {this.balanceService = balanceService;}

    @GetMapping("/") @Operation(summary = "Get first balance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "First balance received",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BalanceDto.class)
                    )}),
            @ApiResponse(responseCode = "404", description = "Reception Failed",
                    content = @Content)
    })
    public ResponseEntity<BalanceDto> getFirstBalance() {
        LOGGER.info("REST request to get first balance");
        LOGGER.debug("Entering getFirstBalance endpoint");
        
        BalanceDto balance = balanceService.getFirstBalance();
        
        LOGGER.info("Successfully retrieved balance with ID: {}", balance.id());
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }
}
