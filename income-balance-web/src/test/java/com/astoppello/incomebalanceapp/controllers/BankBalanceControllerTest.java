package com.astoppello.incomebalanceapp.controllers;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.BankDTO;
import com.astoppello.incomebalanceapp.services.BankBalanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class BankBalanceControllerTest {

    private static final Long ID = 1L;
    private static final String NAME = "Revolut";
    private static final BigDecimal RESULT = BigDecimal.valueOf(100);
    @InjectMocks
    BankBalanceController controller;
    @Mock
    BankBalanceService bankBalanceService;
    MockMvc mockMvc;
    BankBalanceDTO bankBalanceDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                                 .build();
        bankBalanceDTO = new BankBalanceDTO();
        bankBalanceDTO.setId(ID);
        bankBalanceDTO.setResult(RESULT);
        BankDTO bankDTO = new BankDTO();
        bankDTO.setName(NAME);
        bankDTO.setId(ID);
        bankBalanceDTO.setBank(bankDTO);
    }

    @Test
    void findAllBankBalances() throws Exception {
        List<BankBalanceDTO> bankBalanceDTOS = List.of(new BankBalanceDTO(), new BankBalanceDTO());
        when(bankBalanceService.findAll()).thenReturn(bankBalanceDTOS);
        mockMvc.perform(get(BankBalanceController.BASE_URL).contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.bankbalances", hasSize(2)));
    }

    @Test
    void findBankBalanceById() throws Exception {
        when(bankBalanceService.findById(anyLong())).thenReturn(bankBalanceDTO);
        mockMvc.perform(get(BankBalanceController.BASE_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", equalTo(1)))
               .andExpect(jsonPath("$.result", equalTo(100)))
               .andExpect(jsonPath("$.bank.name", equalTo(NAME)))
               .andExpect(jsonPath("$.bank.id", equalTo(1)));
    }
}