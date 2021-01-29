package com.astoppello.incomebalanceapp.controllers;

import com.astoppello.incomebalanceapp.dto.domain.YearBalanceDTO;
import com.astoppello.incomebalanceapp.services.YearBalanceService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class YearBalanceControllerTest {

  private final int YEAR = 2020;
  private final Long ID = 1L;

  @InjectMocks YearBalanceController yearBalanceController;
  @Mock YearBalanceService yearBalanceService;
  MockMvc mockMvc;
  YearBalanceDTO yearBalanceDTO;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(yearBalanceController).build();
    yearBalanceDTO = new YearBalanceDTO();
    yearBalanceDTO.setId(ID);
    yearBalanceDTO.setYear(YEAR);
  }

  @Test
  void findAllYearBalance() throws Exception {
    List<YearBalanceDTO> yearBalances = List.of(new YearBalanceDTO(), new YearBalanceDTO());
    when(yearBalanceService.findAll()).thenReturn(yearBalances);
    mockMvc
        .perform(get(YearBalanceController.BASE_URL + "/").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.yearBalances", hasSize(2)));
    verify(yearBalanceService).findAll();
  }

  @Test
  void findYearBalanceById() throws Exception {
    when(yearBalanceService.findById(ID)).thenReturn(yearBalanceDTO);
    mockMvc
        .perform(
            get(YearBalanceController.BASE_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", equalTo(1)));
    verify(yearBalanceService).findById(anyLong());
  }

  @Test
  void findYearBalanceByYear() throws Exception {
    when(yearBalanceService.findYearBalanceByYear(YEAR)).thenReturn(yearBalanceDTO);
    mockMvc
        .perform(
            get(YearBalanceController.BASE_URL + "?year=2020")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.year", equalTo(YEAR)));
  }

  @Test
  void createNewYearBalance() throws Exception {
    YearBalanceDTO yearBalanceCreated = new YearBalanceDTO();
    yearBalanceCreated.setYear(yearBalanceDTO.getYear());
    yearBalanceCreated.setId(yearBalanceDTO.getId());
    when(yearBalanceService.createNewYearBalance(yearBalanceDTO)).thenReturn(yearBalanceCreated);
    mockMvc
        .perform(
            post(YearBalanceController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractRestControllerTest.asJsonString(yearBalanceDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", equalTo(1)))
        .andExpect(jsonPath("$.year", equalTo(YEAR)));
    verify(yearBalanceService).createNewYearBalance(any(YearBalanceDTO.class));
  }

  @Test
  void saveYearBalance() throws Exception {
    YearBalanceDTO yearBalanceDTOToSave = yearBalanceDTO;
    yearBalanceDTOToSave.setSalary(new BigDecimal(200));
    when(yearBalanceService.saveYearBalance(ID, yearBalanceDTO)).thenReturn(yearBalanceDTOToSave);
    mockMvc
        .perform(
            put(YearBalanceController.BASE_URL + "/" + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractRestControllerTest.asJsonString(yearBalanceDTOToSave)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", equalTo(1)))
        .andExpect(jsonPath("$.year", equalTo(YEAR)))
        .andExpect(jsonPath("$.salary", equalTo(200)));
    verify(yearBalanceService, times(1)).saveYearBalance(anyLong(), any(YearBalanceDTO.class));
  }

  @Test
  void patchYearBalance() throws Exception {
    YearBalanceDTO saved = yearBalanceDTO;
    saved.setResult(new BigDecimal(200));
    when(yearBalanceService.updateYearBalance(ID, yearBalanceDTO)).thenReturn(saved);
    mockMvc
        .perform(
            patch(YearBalanceController.BASE_URL + "/" + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractRestControllerTest.asJsonString(yearBalanceDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", equalTo(1)))
        .andExpect(jsonPath("$.year", equalTo(YEAR)))
        .andExpect(jsonPath("$.result", equalTo(200)));
    verify(yearBalanceService, times(1)).updateYearBalance(anyLong(), any(YearBalanceDTO.class));
  }

  @Test
  void deleteYearBalance() throws Exception {
    mockMvc.perform(delete(YearBalanceController.BASE_URL + "/" + ID)).andExpect(status().isOk());
    verify(yearBalanceService, times(1)).deleteYearBalance(anyLong());
  }
}
