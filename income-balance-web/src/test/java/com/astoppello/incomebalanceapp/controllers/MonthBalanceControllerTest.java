package com.astoppello.incomebalanceapp.controllers;

import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceDTO;
import com.astoppello.incomebalanceapp.services.MonthBalanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MonthBalanceControllerTest {

  private static final Long ID = 1L;
  private static final String MONTH = "September";

  @InjectMocks MonthBalanceController monthBalanceController;
  @Mock MonthBalanceService monthBalanceService;
  MockMvc mockMvc;
  MonthBalanceDTO monthBalanceDTO;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(monthBalanceController).build();
    monthBalanceDTO = new MonthBalanceDTO();
    monthBalanceDTO.setId(ID);
    monthBalanceDTO.setMonth(MONTH);
  }

  @Test
  void findAllMonthBalance() throws Exception {
    List<MonthBalanceDTO> monthBalanceDTOS = List.of(new MonthBalanceDTO(), new MonthBalanceDTO());
    when(monthBalanceService.findAllById(anyLong())).thenReturn(monthBalanceDTOS);
    mockMvc
        .perform(
            get(YearBalanceController.BASE_URL + "/1/monthBalances/")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.monthBalances", hasSize(2)));
    verify(monthBalanceService).findAllById(anyLong());
  }

  @Test
  void findMonthBalanceById() throws Exception {
    when(monthBalanceService.findMonthOfYearById(anyLong(), anyLong())).thenReturn(monthBalanceDTO);
    mockMvc
        .perform(
            get(YearBalanceController.BASE_URL + "/1/monthBalances/" + ID)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", equalTo(1)));
    verify(monthBalanceService).findMonthOfYearById(anyLong(), anyLong());
  }

  @Test
  void findMonthBalanceByMonth() throws Exception {
    when(monthBalanceService.findByMonth(anyString())).thenReturn(List.of(monthBalanceDTO));
    mockMvc
        .perform(
            get(MonthBalanceController.BASE_URL + "?month=September")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractRestControllerTest.asJsonString(MONTH)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.monthBalances.[0].month", equalTo(MONTH)));
  }

  @Test
  void createNewMonthBalance() throws Exception {
    when(monthBalanceService.createNewMonthBalanceById(anyLong(), any(MonthBalanceDTO.class)))
        .thenReturn(monthBalanceDTO);
    mockMvc
        .perform(
            post(YearBalanceController.BASE_URL + "/1/monthBalances")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractRestControllerTest.asJsonString(monthBalanceDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", equalTo(1)))
        .andExpect(jsonPath("$.month", equalTo(MONTH)));
    verify(monthBalanceService).createNewMonthBalanceById(anyLong(), any(MonthBalanceDTO.class));
  }

  @Test
  void testFindAllMonthBalance() throws Exception {
    when(monthBalanceService.findAll()).thenReturn(List.of(monthBalanceDTO));
    mockMvc
        .perform(get(MonthBalanceController.BASE_URL + "/").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.monthBalances", hasSize(1)))
        .andExpect(jsonPath("$.monthBalances.[0].month", equalTo(MONTH)))
        .andExpect(jsonPath("$.monthBalances.[0].id", equalTo(1)));
    verify(monthBalanceService, times(1)).findAll();
  }

  @Test
  void testFindMonthBalanceById() throws Exception {
    when(monthBalanceService.findById(anyLong())).thenReturn(monthBalanceDTO);
    mockMvc
        .perform(
            get(MonthBalanceController.BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.month", equalTo(MONTH)))
        .andExpect(jsonPath("$.id", equalTo(1)));
    verify(monthBalanceService, times(1)).findById(anyLong());
  }

  @Test
  void testCreateNewMonthBalance() throws Exception {
    when(monthBalanceService.createNewMonthBalance(any(MonthBalanceDTO.class)))
        .thenReturn(monthBalanceDTO);
    mockMvc
        .perform(
            post(MonthBalanceController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractRestControllerTest.asJsonString(monthBalanceDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", equalTo(1)))
        .andExpect(jsonPath("$.month", equalTo(MONTH)));
    verify(monthBalanceService).createNewMonthBalance(any(MonthBalanceDTO.class));
  }
}
