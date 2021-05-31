package com.astoppello.incomebalanceapp.ITcontroller;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.BankBalanceSetDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.astoppello.incomebalanceapp.controllers.BankBalanceController.BASE_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Created by @author stopp on 31/05/2021
 */
@SpringBootTest
@AutoConfigureMockMvc
public class BankBalanceControllerIT {

    @Autowired
    private MockMvc mockMvc;
    private Long ID = 1L;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getAllBankBalancesTest() throws Exception {
        MvcResult result = mockMvc.perform(get(BASE_URL + "/").contentType(MediaType.APPLICATION_JSON)).andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        BankBalanceSetDTO bankBalanceSetDTO = new ObjectMapper().readValue(result.getResponse().getContentAsString(),
            BankBalanceSetDTO.class);
        assertThat(bankBalanceSetDTO).isNotNull();
        assertThat(bankBalanceSetDTO.getBankBalances()).isNotNull();
        assertThat(bankBalanceSetDTO.getBankBalances().size()).isEqualTo(1);
    }


    @Test
    void getBankBalanceByIdTest() throws Exception {
        MvcResult result = mockMvc.perform(get(BASE_URL + "/"+ID).contentType(MediaType.APPLICATION_JSON)).andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        String actualResponse = result.getResponse().getContentAsString();
        BankBalanceDTO bankBalanceDTO = new ObjectMapper().readValue(actualResponse, BankBalanceDTO.class);

        assertThat(bankBalanceDTO).isNotNull();
        assertThat(bankBalanceDTO.getId()).isEqualTo(1);
        assertThat(bankBalanceDTO.getIncomes()).isEqualTo("150.00");
        assertThat(bankBalanceDTO.getExpenses()).isEqualTo("100.00");
        assertThat(bankBalanceDTO.getMonthBalanceId()).isEqualTo(3);
        assertThat(bankBalanceDTO.getYearBalanceId()).isEqualTo(3);
        assertThat(bankBalanceDTO.getBank()).isNotNull();
        assertThat(bankBalanceDTO.getBank().getId()).isEqualTo(1);
        assertThat(bankBalanceDTO.getBank().getName()).isEqualTo("Revolut");

    }

}
