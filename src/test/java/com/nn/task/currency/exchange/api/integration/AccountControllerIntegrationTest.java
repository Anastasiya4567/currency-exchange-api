package com.nn.task.currency.exchange.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nn.task.currency.exchange.api.openapi.model.CreateAccountRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.nn.task.currency.exchange.api.testutil.TestConstants.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void accountsPost_shouldCreateAccountAndReturnDetails() throws Exception {
        // given
        CreateAccountRequest request = new CreateAccountRequest();
        request.setFirstName(FIRST_NAME);
        request.setLastName(LAST_NAME);
        request.setInitialBalancePLN(BALANCE_PLN);

        // when // then
        mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME)))
                .andExpect(jsonPath("$.balancePLN", is(BALANCE_PLN)))
                .andExpect(jsonPath("$.balanceUSD", is("0.00")));
    }

    @Test
    void accountsIdGet_shouldReturn404ForNonexistentAccount() throws Exception {
        // given
        UUID randomId = UUID.randomUUID();

        // when // then
        mockMvc.perform(get("/accounts/{id}", randomId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("ACCOUNT_NOT_FOUND")))
                .andExpect(jsonPath("$.message", containsString("Account not found")));
    }
}

