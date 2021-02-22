package com.astoppello.incomebalanceapp.controllers;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.BankDTO;
import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceDTO;
import com.astoppello.incomebalanceapp.exceptions.ResourceNotFoundException;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BankBalanceControllerTest {

  final Long ID = 1L;
  final String REVOLUT = "Revolut";
  private String result = "100";
  final BigDecimal RESULT = new BigDecimal(result);
  @InjectMocks BankBalanceController controller;
  @Mock BankBalanceService bankBalanceService;
  MockMvc mockMvc;
  BankBalanceDTO bankBalanceDTO;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(new RestResponseEntityExceptionHanlder())
            .build();
    bankBalanceDTO = new BankBalanceDTO();
    bankBalanceDTO.setId(ID);
    bankBalanceDTO.setResult(result);
    BankDTO bankDTO = new BankDTO();
    bankDTO.setName(REVOLUT);
    bankDTO.setId(ID);
    bankBalanceDTO.setBank(bankDTO);
  }

  @Test
  void findAllBankBalancesById() throws Exception {
    List<BankBalanceDTO> bankBalanceDTOS = List.of(new BankBalanceDTO(), new BankBalanceDTO());
    when(bankBalanceService.findAllByIds(anyLong(), anyLong())).thenReturn(bankBalanceDTOS);
    mockMvc
        .perform(
            get(YearBalanceController.BASE_URL + "/1/monthBalances/1/bankBalances/")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.bankBalances", hasSize(2)));
    verify(bankBalanceService).findAllByIds(anyLong(), anyLong());
  }

  @Test
  void findAllBankBalancesByYearBalanceId() throws Exception {
    List<BankBalanceDTO> bankBalanceDTOS = List.of(new BankBalanceDTO(), new BankBalanceDTO());
    when(bankBalanceService.findAllByYearBalanceId(anyLong())).thenReturn(bankBalanceDTOS);
    mockMvc
            .perform(
                    get(YearBalanceController.BASE_URL + "/1/bankBalances/")
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.bankBalances", hasSize(2)));
    verify(bankBalanceService).findAllByYearBalanceId(anyLong());
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
        .andExpect(jsonPath("$.result", equalTo(result)))
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
        .andExpect(jsonPath("$.bankBalances.[0].result", equalTo(result)))
        .andExpect(jsonPath("$.bankBalances.[0].bank.name", equalTo(REVOLUT)))
        .andExpect(jsonPath("$.bankBalances.[0].bank.id", equalTo(1)));
  }

  @Test
  void createNewBankBalance() throws Exception {
    bankBalanceDTO.setMonthBalanceId(ID);
    when(bankBalanceService.createNewBankBalance(bankBalanceDTO))
        .thenReturn(bankBalanceDTO);
    mockMvc
        .perform(
            post(BankBalanceController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractRestControllerTest.asJsonString(bankBalanceDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", equalTo(1)))
        .andExpect(jsonPath("$.result", equalTo(result)))
        .andExpect(jsonPath("$.monthBalanceId", equalTo(1)))
        .andExpect(jsonPath("$.bank.name", equalTo(REVOLUT)));
    verify(bankBalanceService).createNewBankBalance(any(BankBalanceDTO.class));
  }

  @Test
  void saveBankBalance() throws Exception {
    BankBalanceDTO bankBalanceDTO1 = new BankBalanceDTO();
    bankBalanceDTO1.setId(3L);
    when(bankBalanceService.saveBankBalance(3L, bankBalanceDTO))
        .thenReturn(bankBalanceDTO1);
    mockMvc
        .perform(
            put(BankBalanceController.BASE_URL + "/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractRestControllerTest.asJsonString(bankBalanceDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", equalTo(3)))
        .andExpect(jsonPath("$.result", equalTo(null)));
    verify(bankBalanceService).saveBankBalance(anyLong(), any(BankBalanceDTO.class));
  }

  @Test
  void updateBankBalance() throws Exception {
    BankBalanceDTO bankBalanceDTO1 = bankBalanceDTO;
    String expenses = "200";
    bankBalanceDTO1.setExpenses(expenses);
    when(bankBalanceService.updateBankBalance(ID, bankBalanceDTO))
        .thenReturn(bankBalanceDTO1);
    mockMvc
        .perform(
            patch(BankBalanceController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractRestControllerTest.asJsonString(bankBalanceDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", equalTo(1)))
        .andExpect(jsonPath("$.result", equalTo(result)))
        .andExpect(jsonPath("$.bank.name", equalTo(REVOLUT)))
        .andExpect(jsonPath("$.bank.id", equalTo(1)))
        .andExpect(jsonPath("$.expenses", equalTo(expenses)));
    verify(bankBalanceService, times(1)).updateBankBalance(anyLong(), any(BankBalanceDTO.class));
  }

  @Test
  void deleteBankBalance() throws Exception {
    mockMvc
        .perform(
            delete(BankBalanceController.BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    verify(bankBalanceService, times(1)).deleteBankBalance(anyLong());
  }

  @Test
  void testNonFoundException() throws Exception {
    when(bankBalanceService.findById(anyLong())).thenThrow(ResourceNotFoundException.class);
    mockMvc
        .perform(
            get(BankBalanceController.BASE_URL + "/111").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void createNewBankBalancesById() throws Exception {
    BankBalanceDTO savedDto = bankBalanceDTO;
    savedDto.setId(ID);
    savedDto.setMonthBalanceId(ID);
    when(bankBalanceService.createNewBankBalanceById(
            ID, ID, bankBalanceDTO))
        .thenReturn(savedDto);
    mockMvc
        .perform(
            post(YearBalanceController.BASE_URL + "/1/monthBalances/1/bankBalances")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractRestControllerTest.asJsonString(bankBalanceDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", equalTo(1)))
        .andExpect(jsonPath("$.result", equalTo(result)))
        .andExpect(jsonPath("$.monthBalanceId", equalTo(1)))
        .andExpect(jsonPath("$.bank.name", equalTo(REVOLUT)));
    verify(bankBalanceService)
        .createNewBankBalanceById(anyLong(), anyLong(), any(BankBalanceDTO.class));
  }
}
