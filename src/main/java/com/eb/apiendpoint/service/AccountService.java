package com.eb.apiendpoint.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AccountService {
    private final Map<String, BigDecimal> accounts = new ConcurrentHashMap<>();

    public void reset(){
        accounts.clear();
    }

    public Optional<BigDecimal> getBalance(String accountId) {
        return Optional.ofNullable(accounts.get(accountId));
    }
}
