package com.astoppello.incomebalanceapp.controllers;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.BankDTO;
import com.astoppello.incomebalanceapp.model.Bank;
import com.astoppello.incomebalanceapp.model.BankBalance;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import com.astoppello.incomebalanceapp.model.YearBalance;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BankBalanceControllerTest {

  private static final Long ID = 1L;
  private static final String REVOLUT = "Revolut";
  private static final BigDecimal RESULT = BigDecimal.valueOf(100);
  private static final BigDecimal EXPENSES = BigDecimal.valueOf(150);
  @InjectMocks BankBalanceController controller;
  @Mock BankBalanceService bankBalanceService;
  MockMvc mockMvc;
  BankBalanceDTO bankBalanceDTO;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    bankBalanceDTO = new BankBalanceDTO();
    bankBalanceDTO.setId(ID);
    bankBalanceDTO.setResult(RESULT);
    BankDTO bankDTO = new BankDTO();
    bankDTO.setName(REVOLUT);
    bankDTO.setId(ID);
    bankBalanceDTO.setBank(bankDTO);
  }

  @Test
  void findAllBankBalances() throws Exception {
    List<BankBalanceDTO> bankBalanceDTOS = List.of(new BankBalanceDTO(), new BankBalanceDTO());
    when(bankBalanceService.findAll(anyLong(), anyLong())).thenReturn(bankBalanceDTOS);
    mockMvc
        .perform(
            get(YearBalanceController.BASE_URL + "/1/monthBalances/1/bankBalances")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.bankbalances", hasSize(2)));
  }

  @Test
  void findBankBalanceById() throws Exception {
    when(bankBalanceService.findById(anyLong(), anyLong(), anyLong())).thenReturn(bankBalanceDTO);
    mockMvc
        .perform(
            get(YearBalanceController.BASE_URL + "/1/monthBalances/1/bankBalances/1")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", equalTo(1)))
        .andExpect(jsonPath("$.result", equalTo(100)))
        .andExpect(jsonPath("$.bank.name", equalTo(REVOLUT)))
        .andExpect(jsonPath("$.bank.id", equalTo(1)));
  }

  @Test
  void findBankBalanceByBankName() throws Exception {
    when(bankBalanceService.findByBankName(anyLong(), anyLong(), anyString()))
        .thenReturn(bankBalanceDTO);
    mockMvc
        .perform(
            post(YearBalanceController.BASE_URL + "/1/monthBalances/1/bankBalances")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractRestControllerTest.asJsonString("Revolut")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", equalTo(1)))
        .andExpect(jsonPath("$.result", equalTo(100)))
        .andExpect(jsonPath("$.bank.name", equalTo(REVOLUT)))
        .andExpect(jsonPath("$.bank.id", equalTo(1)));
  }
}
