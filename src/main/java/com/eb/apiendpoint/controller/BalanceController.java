package com.eb.apiendpoint.controller;

import com.eb.apiendpoint.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
public class BalanceController {

    private final AccountService accountService;

    public BalanceController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getBalance(@RequestParam("account_id") String accountId) {
        Optional<BigDecimal> balance = accountService.getBalance(accountId);
        if (balance.isPresent()) {
            return ResponseEntity.ok(balance.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BigDecimal.ZERO);
    }
}
