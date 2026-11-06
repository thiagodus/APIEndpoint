package com.eb.apiendpoint.service;

import com.eb.apiendpoint.dto.TransferResponse;
import com.eb.apiendpoint.model.Account;
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

    public synchronized Account deposit (String destinationId, BigDecimal amount){
        BigDecimal currentBalance = accounts.getOrDefault(destinationId, BigDecimal.ZERO);
        BigDecimal newBalance = currentBalance.add(amount);
        accounts.put(destinationId, newBalance);
        return new Account(destinationId, newBalance);
    }

    public synchronized Optional<Account> withdraw(String originId, BigDecimal amount){
        if(!accounts.containsKey(originId)){
            return Optional.empty();
        }
        BigDecimal currentBalance = accounts.get(originId);
        BigDecimal newBalance = currentBalance.subtract(amount);
        accounts.put(originId, newBalance);
        return Optional.of(new Account(originId, newBalance));
    }

    public synchronized Optional<TransferResponse> transfer(String originId, String destinationId, BigDecimal amount){
        if(!accounts.containsKey(originId)){
            return Optional.empty();
        }
        Account originAccount = withdraw(originId, amount).orElseThrow();
        Account destinationAccount = deposit(destinationId, amount);
        return Optional.of(new TransferResponse(originAccount, destinationAccount));
    }
}
