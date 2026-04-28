package com.nn.task.currency.exchange.api.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nn.task.currency.exchange.api.openapi.model.CreateAccountRequest;
import com.nn.task.currency.exchange.api.openapi.model.ExchangeRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;

import static com.nn.task.currency.exchange.api.domain.model.Currency.PLN;
import static com.nn.task.currency.exchange.api.domain.model.Currency.USD;
import static com.nn.task.currency.exchange.api.testutil.TestConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ActiveProfiles({"test"})
@AutoConfigureMockMvc
public class AccountControllerIntegrationTest {
    private static final String PATH_ACCOUNTS = "/accounts";
    private static final String BALANCE = "100.00";

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldCreateAccountSuccessfully() throws Exception {
        // given
        var request = new CreateAccountRequest(FIRST_NAME, LAST_NAME, BALANCE);

        // when / then
        mockMvc.perform(post(PATH_ACCOUNTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(LAST_NAME))
            .andExpect(jsonPath("$.balancePLN").value(BALANCE))
            .andExpect(jsonPath("$.balanceUSD").value(BigDecimal.ZERO));
    }

    @Test
    void shouldReturnBadRequestForNegativeBalanceOnCreate() throws Exception {
        // given
        var request = new CreateAccountRequest(FIRST_NAME, LAST_NAME, NEGATIVE_AMOUNT);

        // when / then
        assertErrorResponse(mockMvc.perform(post(PATH_ACCOUNTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest()));
    }

    @Test
    void shouldGetAccountSuccessfully() throws Exception {
        // given
        String id = createAccountAndGetId(BALANCE);

        // when
        var resultActions = mockMvc.perform(get(PATH_ACCOUNTS + "/" + id));

        // then
        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(LAST_NAME))
            .andExpect(jsonPath("$.balancePLN").value("100.0000"));
    }

    @Test
    void shouldReturnNotFoundForNonExistingAccount() throws Exception {
        // given
        var nonExistingId = "123e4567-e89b-12d3-a456-426614174000";

        // when / then
        assertErrorResponse(mockMvc.perform(get(PATH_ACCOUNTS + "/" + nonExistingId))
            .andExpect(status().isBadRequest()));
    }

    @Test
    void shouldExchangeCurrencySuccessfully() throws Exception {
        // given
        String id = createAccountAndGetId(BALANCE);

        // when
        var exchangeRequest = new ExchangeRequest(PLN.name(), USD.name(), AMOUNT_10);
        var resultActions = mockMvc.perform(post(PATH_ACCOUNTS + "/" + id + "/exchange")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(exchangeRequest)));

        // then
        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(LAST_NAME))
            .andExpect(jsonPath("$.balancePLN").exists())
            .andExpect(jsonPath("$.balanceUSD").exists());
    }

    @Test
    void shouldReturnBadRequestForInsufficientBalanceOnExchange() throws Exception {
        // given
        String id = createAccountAndGetId(AMOUNT_5);

        // when / then
        var exchangeRequest = new ExchangeRequest(PLN.name(), USD.name(), AMOUNT_10);
        assertErrorResponse(mockMvc.perform(post(PATH_ACCOUNTS + "/" + id + "/exchange")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exchangeRequest)))
            .andExpect(status().isBadRequest()));
    }

    private String createAccountAndGetId(String balance) throws Exception {
        var request = new CreateAccountRequest(FIRST_NAME, LAST_NAME, balance);
        var response = mockMvc.perform(post(PATH_ACCOUNTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).get("id").asText();
    }

    private void assertErrorResponse(ResultActions resultActions) throws Exception {
        resultActions
            .andExpect(jsonPath("$.code").exists())
            .andExpect(jsonPath("$.message").exists());
    }
}
