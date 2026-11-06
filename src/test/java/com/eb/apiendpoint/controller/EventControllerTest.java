package com.eb.apiendpoint.controller;

import com.eb.apiendpoint.model.Account;
import com.eb.apiendpoint.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService.reset();
    }

    @Test
    void shouldCreateAccountOnDeposit() throws Exception {

        String jsonPayload = """
                {
                    "type": "deposit",
                    "destination": "100",
                    "amount": 10
                }
                """;

        mockMvc.perform(post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.destination.id").value("100"))
                .andExpect(jsonPath("$.destination.balance").value(10));

    }

    @Test
    void shouldReturnNotFoundWhenWithdrawingFromNonExistingAccount () throws Exception {
        String jsonPayload = """
                {
                    "type": "withdraw",
                    "origin": "200",
                    "amount": 10
                }
                """;
        mockMvc.perform(post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isNotFound())
                .andExpect(content().string("0"));

    }

    @Test
    void shouldWithdrawFromExistingAccount() throws Exception {
        String depositPayload = """
                {
                    "type": "deposit",
                    "destination": "100",
                    "amount": 20
                }
                """;
        mockMvc.perform(post("/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(depositPayload));

        // Now withdraw 5 from account 100
        String withdrawPayload = """
                {
                    "type": "withdraw",
                    "origin": "100",
                    "amount": 5
                }
                """;

        mockMvc.perform(post("/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(withdrawPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.origin.id").value("100"))
                .andExpect(jsonPath("$.origin.balance").value(15));

    }

    @Test
    void shouldReturn404WhenTransferringFromNonExistingAccount()  throws Exception {
        String jsonPayload = """
                {
                    "type": "transfer",
                    "origin": "200",
                    "destination": "300",
                    "amount": 15
                }
                """;
        mockMvc.perform(post("/event")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonPayload))
        .andExpect(status().isNotFound())
        .andExpect(content().string("0"));
    }

    @Test
    void shouldTransferFromExistingAccountToNewAccount()  throws Exception {
        String depositPayload = """
                {
                    "type": "deposit",
                    "destination": "100",
                    "amount": 15
                }
                """;
        mockMvc.perform(post("/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(depositPayload));


        String transferPayload = """
                {
                    "type": "transfer",
                    "origin": "100",
                    "destination": "300",
                    "amount": 15
                }
                """;

        mockMvc.perform(post("/event")
            .contentType(MediaType.APPLICATION_JSON)
                .content(transferPayload))
                .andExpect(status().isCreated())
        .andExpect(jsonPath("$.origin.id").value("100"))
        .andExpect(jsonPath("$.origin.balance").value(BigDecimal.ZERO))
        .andExpect(jsonPath("$.destination.id").value("300"))
        .andExpect(jsonPath("$.destination.balance").value(15));

    }




}
