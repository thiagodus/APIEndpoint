package com.eb.apiendpoint.controller;

import com.eb.apiendpoint.dto.EventRequest;
import com.eb.apiendpoint.dto.TransferResponse;
import com.eb.apiendpoint.model.Account;
import com.eb.apiendpoint.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@RestController
public class EventController {
    private final AccountService accountService;

    public EventController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/event")
    public ResponseEntity<?> handleEvent(@RequestBody EventRequest eventRequest) {
        return switch (eventRequest.type()){
            case "deposit" -> {
                Account account = accountService.deposit(eventRequest.destination(), eventRequest.amount());
                yield ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(Map.of("destination", account));
            }
            case  "withdraw" -> {
                Optional<Account> account = accountService.withdraw(eventRequest.origin(), eventRequest.amount());
                if (account.isPresent()) {
                    yield ResponseEntity.status(HttpStatus.CREATED).body(Map.of("origin", account.get()));
                }
                yield ResponseEntity.status(HttpStatus.NOT_FOUND).body(BigDecimal.ZERO);
            }

            case "transfer" -> {
                Optional<TransferResponse> response =
                        accountService.transfer(
                                eventRequest.origin(),
                                eventRequest.destination(),
                                eventRequest.amount());
                if (response.isPresent()) {
                    yield ResponseEntity.status(HttpStatus.CREATED)
                            .body(Map.of("origin", response.get().origin(),
                                    "destination", response.get().destination()
                                    )
                            );
                }
                yield ResponseEntity.status(HttpStatus.NOT_FOUND).body(BigDecimal.ZERO);
            }
            default -> ResponseEntity.badRequest().build();
        };

    }
}
