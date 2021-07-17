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

import java.math.BigDecimal;
import java.time.Month;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MonthBalanceControllerTest {

    private static final Long ID = 1L;
    private static final String MONTH = "SEPTEMBER";

    @InjectMocks
    MonthBalanceController monthBalanceController;
    @Mock
    MonthBalanceService monthBalanceService;
    MockMvc mockMvc;
    MonthBalanceDTO monthBalanceDTO;
    private List<MonthBalanceDTO> monthBalanceDTOS;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(monthBalanceController).build();
        monthBalanceDTO = new MonthBalanceDTO();
        monthBalanceDTO.setId(ID);
        monthBalanceDTO.setMonth(Month.valueOf(MONTH));
        monthBalanceDTO.setYearBalanceId(ID);
        monthBalanceDTO.setSavingPercentage(40.00);

        MonthBalanceDTO monthBalanceDTO1 = new MonthBalanceDTO();
        monthBalanceDTO1.setMonth(Month.DECEMBER);
        monthBalanceDTO1.setId(2L);
        monthBalanceDTOS = List.of(monthBalanceDTO, monthBalanceDTO1);
    }

    @Test
    void findAllMonthBalance() throws Exception {
        when(monthBalanceService.findAllById(ID)).thenReturn(monthBalanceDTOS);
        mockMvc
                .perform(get(YearBalanceController.BASE_URL + "/1/monthBalances/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monthBalances", hasSize(2)))
                .andExpect(jsonPath("$.monthBalances.[0].month", equalTo(MONTH)));
        verify(monthBalanceService).findAllById(anyLong());
    }

    @Test
    void findMonthBalanceByMonth() throws Exception {
        when(monthBalanceService.findByMonth(MONTH)).thenReturn(List.of(monthBalanceDTO));
        mockMvc
                .perform(get(MonthBalanceController.BASE_URL + "?month=" + MONTH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monthBalances.[0].month", equalTo(MONTH)));
    }

    @Test
    void createNewMonthBalanceById() throws Exception {
        when(monthBalanceService.createNewMonthBalanceById(ID, monthBalanceDTO)).thenReturn(monthBalanceDTO);
        mockMvc
                .perform(post(YearBalanceController.BASE_URL + "/1/monthBalances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AbstractRestControllerTest.asJsonString(monthBalanceDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.month", equalTo(MONTH)))
                .andExpect(jsonPath("$.savingPercentage", equalTo(40.0)));
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
        when(monthBalanceService.findById(ID)).thenReturn(monthBalanceDTO);
        mockMvc
                .perform(get(MonthBalanceController.BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.month", equalTo(MONTH)))
                .andExpect(jsonPath("$.id", equalTo(1)));
        verify(monthBalanceService, times(1)).findById(anyLong());
    }

    @Test
    void saveMonthBalance() throws Exception {
        MonthBalanceDTO savedMonthBalanceDto = monthBalanceDTO;
        savedMonthBalanceDto.setYearBalanceId(ID);
        when(monthBalanceService.saveMonthBalance(ID, monthBalanceDTO)).thenReturn(savedMonthBalanceDto);
        mockMvc
                .perform(put(MonthBalanceController.BASE_URL + "/1")
                        .content(AbstractRestControllerTest.asJsonString(savedMonthBalanceDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.month", equalTo(MONTH)))
                .andExpect(jsonPath("$.yearBalanceId", equalTo(1)));
        verify(monthBalanceService, times(1)).saveMonthBalance(anyLong(), any(MonthBalanceDTO.class));
    }

    @Test
    void updateMonthBalance() throws Exception {
        MonthBalanceDTO monthBalanceDTO1 = monthBalanceDTO;
        String incomes = "200";
        monthBalanceDTO1.setIncomes(new BigDecimal(incomes));
        when(monthBalanceService.updateMonthBalance(ID, monthBalanceDTO)).thenReturn(monthBalanceDTO1);
        mockMvc
                .perform(patch(MonthBalanceController.BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AbstractRestControllerTest.asJsonString(monthBalanceDTO1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.month", equalTo(MONTH)))
                .andExpect(jsonPath("$.incomes", equalTo(incomes)));
        verify(monthBalanceService, times(1)).updateMonthBalance(anyLong(), any(MonthBalanceDTO.class));
    }

    @Test
    void deleteMonthBalance() throws Exception {
        mockMvc
                .perform(delete(MonthBalanceController.BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(monthBalanceService, times(1)).delete(anyLong());
    }
}
