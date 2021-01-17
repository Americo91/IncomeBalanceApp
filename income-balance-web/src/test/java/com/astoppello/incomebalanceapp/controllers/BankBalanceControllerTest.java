package com.astoppello.incomebalanceapp.controllers;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.BankDTO;
import com.astoppello.incomebalanceapp.exceptions.ResourceNotFoundException;
import com.astoppello.incomebalanceapp.model.BankBalance;
import com.astoppello.incomebalanceapp.services.BankBalanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BankBalanceControllerTest {

  private static final Long ID = 1L;
  private static final String REVOLUT = "Revolut";
  private static final BigDecimal RESULT = BigDecimal.valueOf(100);
  @InjectMocks BankBalanceController controller;
  @Mock BankBalanceService bankBalanceService;
  MockMvc mockMvc;
  BankBalanceDTO bankBalanceDTO;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new RestResponseEntityExceptionHanlder()).build();
    bankBalanceDTO = new BankBalanceDTO();
    bankBalanceDTO.setId(ID);
    bankBalanceDTO.setResult(RESULT);
    BankDTO bankDTO = new BankDTO();
    bankDTO.setName(REVOLUT);
    bankDTO.setId(ID);
    bankBalanceDTO.setBank(bankDTO);
  }

  @Test
  void findAllBankBalancesById() throws Exception {
    List<BankBalanceDTO> bankBalanceDTOS = List.of(new BankBalanceDTO(), new BankBalanceDTO());
    when(bankBalanceService.findAllById(anyLong(), anyLong())).thenReturn(bankBalanceDTOS);
    mockMvc
        .perform(
            get(YearBalanceController.BASE_URL + "/1/monthBalances/1/bankBalances/")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.bankBalances", hasSize(2)));
    verify(bankBalanceService).findAllById(anyLong(), anyLong());
  }

  @Test
  void findAllBankBalances() throws Exception {
    List<BankBalanceDTO> bankBalanceDTOList = List.of(bankBalanceDTO, new BankBalanceDTO());
    when(bankBalanceService.findAll()).thenReturn(bankBalanceDTOList);
    mockMvc
        .perform(get(BankBalanceController.BASE_URL + "/").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.bankBalances", hasSize(2)))
        .andExpect(jsonPath("$.bankBalances.[0].id", equalTo(1)));
    verify(bankBalanceService).findAll();
  }

  @Test
  void findBankBalanceById() throws Exception {
    when(bankBalanceService.findById(anyLong())).thenReturn(bankBalanceDTO);
    mockMvc
        .perform(get(BankBalanceController.BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", equalTo(1)))
        .andExpect(jsonPath("$.result", equalTo(100)))
        .andExpect(jsonPath("$.bank.name", equalTo(REVOLUT)))
        .andExpect(jsonPath("$.bank.id", equalTo(1)));
    verify(bankBalanceService).findById(anyLong());
  }

  @Test
  void findBankBalanceByBankName() throws Exception {
    when(bankBalanceService.findByBankName(anyString())).thenReturn(List.of(bankBalanceDTO));
    mockMvc
        .perform(
            get(BankBalanceController.BASE_URL + "?bankName=Revolut")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.bankBalances", hasSize(1)))
        .andExpect(jsonPath("$.bankBalances.[0].id", equalTo(1)))
        .andExpect(jsonPath("$.bankBalances.[0].result", equalTo(100)))
        .andExpect(jsonPath("$.bankBalances.[0].bank.name", equalTo(REVOLUT)))
        .andExpect(jsonPath("$.bankBalances.[0].bank.id", equalTo(1)));
  }

  @Test
  void createNewBankBalance() throws Exception {
    bankBalanceDTO.setMonthBalanceId(ID);
    when(bankBalanceService.createNewBankBalance(any(BankBalanceDTO.class)))
        .thenReturn(bankBalanceDTO);
    mockMvc
        .perform(
            post(BankBalanceController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractRestControllerTest.asJsonString(bankBalanceDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", equalTo(1)))
        .andExpect(jsonPath("$.result", equalTo(RESULT.intValue())))
        .andExpect(jsonPath("$.monthBalanceId", equalTo(1)))
        .andExpect(jsonPath("$.bank.name", equalTo(REVOLUT)));
    verify(bankBalanceService).createNewBankBalance(any(BankBalanceDTO.class));
  }

  @Test
  void saveBankBalance() throws Exception {
    BankBalanceDTO bankBalanceDTO1 = new BankBalanceDTO();
    bankBalanceDTO1.setId(2L);
    when(bankBalanceService.saveBankBalance(anyLong(), any(BankBalanceDTO.class)))
        .thenReturn(bankBalanceDTO1);
    mockMvc
        .perform(
            put(BankBalanceController.BASE_URL + "/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractRestControllerTest.asJsonString(bankBalanceDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", equalTo(2)))
        .andExpect(jsonPath("$.result", equalTo(null)));
    verify(bankBalanceService).saveBankBalance(anyLong(), any(BankBalanceDTO.class));
  }

  @Test
  void updateBankBalance() throws Exception {
    BankBalanceDTO bankBalanceDTO1 = bankBalanceDTO;
    bankBalanceDTO1.setExpenses(new BigDecimal(200));
    when(bankBalanceService.updateBankBalance(anyLong(), any(BankBalanceDTO.class))).thenReturn(bankBalanceDTO1);
    mockMvc.perform(patch(BankBalanceController.BASE_URL+"/1").contentType(MediaType.APPLICATION_JSON).content(AbstractRestControllerTest.asJsonString(bankBalanceDTO)))
    .andExpect(status().isOk())
           .andExpect(jsonPath("$.id", equalTo(1)))
           .andExpect(jsonPath("$.result", equalTo(100)))
           .andExpect(jsonPath("$.bank.name", equalTo(REVOLUT)))
           .andExpect(jsonPath("$.bank.id", equalTo(1)))
    .andExpect(jsonPath("$.expenses", equalTo(200)));
    verify(bankBalanceService, times(1)).updateBankBalance(anyLong(), any(BankBalanceDTO.class));
  }

  @Test
  void deleteBankBalance() throws Exception{
    mockMvc.perform(delete(BankBalanceController.BASE_URL+"/1").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk());
    verify(bankBalanceService, times(1)).deleteBankBalance(anyLong());
  }

  @Test
  void testNonFoundException() throws Exception {
    when(bankBalanceService.findById(anyLong())).thenThrow(ResourceNotFoundException.class);
    mockMvc.perform(get(BankBalanceController.BASE_URL+"/111").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isNotFound());
  }
}
